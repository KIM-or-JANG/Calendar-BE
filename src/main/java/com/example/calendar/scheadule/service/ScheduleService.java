package com.example.calendar.scheadule.service;

import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.room.entity.Room;
import com.example.calendar.room.entity.RoomUser;
import com.example.calendar.scheadule.dto.*;
import com.example.calendar.scheadule.entity.Comment;
import com.example.calendar.scheadule.entity.Schedule;
import com.example.calendar.room.repository.RoomRepository;
import com.example.calendar.room.repository.RoomUserRepository;
import com.example.calendar.scheadule.repository.CommentRepository;
import com.example.calendar.scheadule.repository.ScheduleRepository;
import com.example.calendar.hoilyDay.sercice.HoliyDaySercice;
import com.example.calendar.common.util.Message;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.user.entity.User;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final HoliyDaySercice holiyDay;
    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;
    private final CommentRepository commentRepository;

    //개인 캘린더
    @Transactional
    public ResponseEntity<Message> getMySchedule(String month, String year, UserDetailsImpl userDetails) throws IOException, ParserConfigurationException, SAXException {
        String localdate = year + month;
        List<RoomUser> roomUserList = roomUserRepository.findByuser_Id(userDetails.getUser().getId());  //사용자가 속해있는 룸 리스트
        List<List<ScheduleListDto>> mainScheduleList = new ArrayList<>();
        for (RoomUser roomUser : roomUserList) {
            List<ScheduleListDto> scheduleList = scheduleRepository.QueryDSL_findAllByRoom_IdAndLocdate(roomUser.getRoom().getId(), localdate);
            mainScheduleList.add(scheduleList);
        }
        CalendarResponseDto calendarResponseDto = new CalendarResponseDto(holiyDay.holiydata(month, year), mainScheduleList, null);
        return new ResponseEntity<>(new Message("개인 일정 목록", calendarResponseDto), HttpStatus.OK);
    }
    //방 캘린더
    @Transactional
    public ResponseEntity<Message> getRoomSchedule(Long roomId, String year, String month, User user) throws IOException, ParserConfigurationException, SAXException {
        String localdate = year + month;
        roomUserRepository.findByuser_IdAndRoom_Id(user.getId(), roomId).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        List<ScheduleListDto> scheduleList = scheduleRepository.QueryDSL_findAllByRoom_IdAndLocdate(roomId, localdate);
        CalendarResponseDto calendarResponseDto = new CalendarResponseDto(holiyDay.holiydata(month, year), null, scheduleList);
        return new ResponseEntity<>(new Message("방 일정 목록", calendarResponseDto), HttpStatus.OK);
    }
    //일정 요청
    @Transactional
    public ResponseEntity<Message> getSchedule(Long scheduleId, ScheduleRequestDto scheduleRequestDto, User user) {
        roomRepository.findById(scheduleRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        roomUserRepository.findByuser_IdAndRoom_Id(user.getId(), scheduleRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        Schedule schedule =  scheduleRepository.findByIdAndRoom_IdAndLocdate(scheduleId, scheduleRequestDto.getRoomId(), scheduleRequestDto.getLocdate()).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        List<Comment> commentList = commentRepository.findAllBySchedule_Id(scheduleId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment.getId(), comment.getComment(), comment.getUserName());
            commentResponseDtoList.add(commentResponseDto);
        }
        ScheduleResponseDto scheduleResponseDto = new ScheduleResponseDto(schedule.getId(), schedule.getRoom().getId(), schedule.getRoom().getRoomName(), schedule.getLocdate(), schedule.getSchedule(), commentResponseDtoList);
        return new ResponseEntity<>(new Message("일정 요청 성공",scheduleResponseDto),HttpStatus.OK);
    }
    //일정 작성
    @Transactional
    public ResponseEntity<Message> createSchedlue(ScheduleRequestDto scheduleRequestDto, UserDetailsImpl userDetails) {
        Room room = roomRepository.findByIdAndRoomName(scheduleRequestDto.getRoomId(), scheduleRequestDto.getRoomName()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        roomUserRepository.findByuser_IdAndRoom_Id(userDetails.getUser().getId(), scheduleRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        Schedule schedule = scheduleRepository.saveAndFlush(new Schedule(scheduleRequestDto.getSchedule(), scheduleRequestDto.getLocdate(), room, userDetails.getUser()));
        return new ResponseEntity<>(new Message("일정 작성 성공", new ScheduleResponseDto(schedule.getId(), schedule.getRoom().getId(), schedule.getRoom().getRoomName(), schedule.getLocdate(), schedule.getSchedule())),HttpStatus.OK);
    }
    //일정 수정
    @Transactional
    public ResponseEntity<Message> updateSchedule(Long scheduleId, ScheduleRequestDto scheduleRequestDto, User user) {
        roomRepository.findById(scheduleRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        roomUserRepository.findByuser_IdAndRoom_Id(user.getId(), scheduleRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        Schedule schedule =  scheduleRepository.findByIdAndRoom_IdAndUser_IdAndLocdate(scheduleId, scheduleRequestDto.getRoomId(), user.getId(), scheduleRequestDto.getLocdate()).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        if(schedule.getUser().getId() == user.getId() || schedule.getRoom().getManager().getId() == user.getId()) {
            schedule.UpdateData(scheduleRequestDto.getSchedule());
            scheduleRepository.saveAndFlush(schedule);
            return new ResponseEntity<>(new Message("일정 수정 성공", new ScheduleResponseDto(
                    schedule.getId(),
                    schedule.getRoom().getId(),
                    schedule.getRoom().getRoomName(),
                    schedule.getLocdate(),
                    schedule.getSchedule())), HttpStatus.OK);
        }
        else {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER);
        }
    }
    //일정 삭제
    @Transactional
    public ResponseEntity<Message> deleteSchedule(Long scheduleId,Long roomId, String locdate, User user) {
        roomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        roomUserRepository.findByuser_IdAndRoom_Id(user.getId(), roomId).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        Schedule schedule =  scheduleRepository.findByIdAndRoom_IdAndUser_IdAndLocdate(scheduleId, roomId, user.getId(), locdate).orElseThrow(
                () -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
        if(schedule.getUser().getId() == user.getId() || schedule.getRoom().getManager().getId() == user.getId()) {
            scheduleRepository.delete(schedule);
            return new ResponseEntity<>(new Message("일정 삭제 성공",new ScheduleResponseDto(
                    schedule.getId(),
                    schedule.getRoom().getId(),
                    schedule.getRoom().getRoomName(),
                    schedule.getLocdate(),
                    schedule.getSchedule())), HttpStatus.OK);
        }
        else {
            throw new CustomException(ErrorCode.FORBIDDEN_MEMBER);
        }
    }
}


