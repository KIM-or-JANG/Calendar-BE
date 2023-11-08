package com.example.calendar.service;

import com.example.calendar.entity.RoomUser;
import com.example.calendar.entity.Schedule;
import com.example.calendar.repository.RoomRepository;
import com.example.calendar.repository.RoomUserRepository;
import com.example.calendar.repository.ScheduleRepository;
import com.example.calendar.dto.ScheaduleResponseDto;
import com.example.calendar.global.hoilyDayApi.HoliyDay;
import com.example.calendar.global.util.Message;
import com.example.calendar.global.security.UserDetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CalendarService {

    private final HoliyDay holiyDay;
    private final ScheduleRepository scheduleRepository;
    private final RoomRepository roomRepository;
    private final RoomUserRepository roomUserRepository;

    //해달 달의 데이터
    public ResponseEntity<Message> getMonthData(String month, String year, UserDetailsImpl userDetails) throws IOException, ParserConfigurationException, SAXException {
        List<RoomUser> roomUserList = roomUserRepository.findByuser_Id(userDetails.getUser().getId());
        List<Schedule> schedules = new ArrayList<>();
        for (int i = 0; i < roomUserList.size() ; i++) {
            schedules = scheduleRepository.findByroom_Id(roomUserList.get(i).getRoom().getId());
        }
        //해당 유저가 포함된 그룹들의 켈린더에서 해당 달의 데이터를 모두 가져와야됨
        ScheaduleResponseDto scheaduleResponseDto = new ScheaduleResponseDto(holiyDay.holiydata(month, year), schedules);
        return new ResponseEntity<>(new Message(null,scheaduleResponseDto),HttpStatus.OK);
    }

}

