package com.example.calendar.scheadule.service;

import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.room.entity.Room;
import com.example.calendar.room.entity.RoomUser;
import com.example.calendar.scheadule.dto.*;
import com.example.calendar.scheadule.entity.Schedule;
import com.example.calendar.room.repository.RoomRepository;
import com.example.calendar.room.repository.RoomUserRepository;
import com.example.calendar.scheadule.repository.ScheduleRepository;
import com.example.calendar.hoilyDay.sercice.HoliyDaySercice;
import com.example.calendar.common.util.Message;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.user.entity.User;
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

    //개인 캘린더
    @Transactional
    public ResponseEntity<Message> getMySchedule(String month, String year,String day, UserDetailsImpl userDetails) throws IOException, ParserConfigurationException, SAXException {
        String localdate = year + month;
        List<RoomUser> roomUserList = roomUserRepository.findByuser_Id(userDetails.getUser().getId());  //사용자가 속해있는 룸 리스트
        List<MyScheduleResponseDto> myscheduleResponseList = new ArrayList<>();
        for (int i = 0; i < roomUserList.size(); i++) {
            Long id = roomUserList.get(i).getRoom().getId();
            List<Schedule> schedules = scheduleRepository.findAllByRoom_IdAndLocdate(id, localdate);

            for (Schedule schedule : schedules) {
                MyRoomResponseDto roomResponse = new MyRoomResponseDto(
                        schedule.getRoom().getId(),
                        schedule.getRoom().getRoomName(),
                        schedule.getRoom().getRoomProfile()
                );
                MyScheduleResponseDto myscheduleResponseDto = new MyScheduleResponseDto(
                        schedule.getSchedule(),
                        schedule.getLocdate(),
                        roomResponse
                );

                myscheduleResponseList.add(myscheduleResponseDto);
            }
        }
        CalendarResponseDto calendarResponseDto = new CalendarResponseDto(holiyDay.holiydata(month, year), myscheduleResponseList);
        return new ResponseEntity<>(new Message(null, calendarResponseDto), HttpStatus.OK);
    }
    //일정 작성
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
    public ResponseEntity<Message> updateSchedule(Long scheduleId, ScheduleRequestDto scheduleRequestDto, User user) {
        roomRepository.findById(scheduleRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
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
    public ResponseEntity<Message> deleteSchedule(Long scheduleId,Long roomId, String locdate, User user) {
        roomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
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
    //방 캘린더
    public ResponseEntity<Message> getRoomSchedule(Long roomId, String year, String month, User user) throws IOException, ParserConfigurationException, SAXException {
        String localdate = year + month;
        roomUserRepository.findByuser_IdAndRoom_Id(user.getId(), roomId).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        List<Schedule> scheduleList = scheduleRepository.findAllByRoom_IdAndLocdate(roomId, localdate);
        List<MyScheduleResponseDto> myscheduleResponseList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            MyScheduleResponseDto myscheduleResponseDto = new MyScheduleResponseDto(
                    schedule.getSchedule(),
                    schedule.getLocdate(),
                    null
            );

            myscheduleResponseList.add(myscheduleResponseDto);
        }
        CalendarResponseDto calendarResponseDto = new CalendarResponseDto(holiyDay.holiydata(month, year), myscheduleResponseList);
        return new ResponseEntity<>(new Message(null, calendarResponseDto), HttpStatus.OK);
    }
}


