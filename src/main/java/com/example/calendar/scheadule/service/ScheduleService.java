package com.example.calendar.scheadule.service;

import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.room.entity.Room;
import com.example.calendar.room.entity.RoomUser;
import com.example.calendar.scheadule.dto.ScheduleRequestDto;
import com.example.calendar.scheadule.dto.ScheduleResponseDto;
import com.example.calendar.scheadule.entity.Schedule;
import com.example.calendar.room.repository.RoomRepository;
import com.example.calendar.room.repository.RoomUserRepository;
import com.example.calendar.scheadule.repository.ScheduleRepository;
import com.example.calendar.scheadule.dto.CalendarResponseDto;
import com.example.calendar.hoilyDay.sercice.HoliyDaySercice;
import com.example.calendar.common.util.Message;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
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

    //해달 달의 데이터
    @Transactional
    public ResponseEntity<Message> getMonthData(String month, String year, UserDetailsImpl userDetails) throws IOException, ParserConfigurationException, SAXException {
        List<RoomUser> roomUserList = roomUserRepository.findByuser_Id(userDetails.getUser().getId());
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < roomUserList.size() ; i++) {
            schedules = scheduleRepository.findByroom_Id(roomUserList.get(i).getRoom().getId());
        }
        //해당 유저가 포함된 그룹들의 켈린더에서 해당 달의 데이터를 모두 가져와야됨
        CalendarResponseDto calendarResponseDto = new CalendarResponseDto(holiyDay.holiydata(month, year), schedules);
        return new ResponseEntity<>(new Message(null,calendarResponseDto),HttpStatus.OK);
    }
    //일정 작성
    public ResponseEntity<Message> createSchedlue(ScheduleRequestDto scheduleRequestDto, UserDetailsImpl userDetails) {
        Room room = roomRepository.findByIdAndRoomName(scheduleRequestDto.getRoomId(), scheduleRequestDto.getRoomName()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        roomUserRepository.findByuser_IdAndRoom_Id(userDetails.getUser().getId(), scheduleRequestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.FORBIDDEN_MEMBER)
        );
        scheduleRepository.saveAndFlush(new Schedule(scheduleRequestDto.getSchedule(), scheduleRequestDto.getLocdate(), room, userDetails.getUser()));
        return new ResponseEntity<>(new Message("일정 작성 성공",new ScheduleResponseDto(room.getId(), room.getRoomName(), scheduleRequestDto.getLocdate(), scheduleRequestDto.getSchedule())),HttpStatus.OK);
    }

}

