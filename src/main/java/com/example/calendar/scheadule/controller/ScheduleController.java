package com.example.calendar.scheadule.controller;

import com.example.calendar.common.util.Message;
import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.scheadule.dto.ScheduleRequestDto;
import com.example.calendar.scheadule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    //해당 달의 데이터
//    @Secured("ROLE_ADMIN") //관리자용 API
    @GetMapping("/calendar")
    public ResponseEntity<Message> getMonthDate(@RequestParam String month, String year,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails ) throws IOException, ParserConfigurationException, SAXException {
        return scheduleService.getMonthData(month, year, userDetails);
    }
    //일정 작성
    @PostMapping("/schedule/create")
    public ResponseEntity<Message> createSchedlue(@RequestBody ScheduleRequestDto scheduleRequestDto,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return scheduleService.createSchedlue(scheduleRequestDto, userDetails);
    }
    //일정 수정
    @PatchMapping("/schedule/update")
    public ResponseEntity<Message> updateSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto,@RequestParam Long scheduleId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return scheduleService.updateSchedule(scheduleId, scheduleRequestDto, userDetails.getUser());
    }
    //일정 삭제
    @DeleteMapping("/schedule/delete")
    public ResponseEntity<Message> deleteSchedule(@RequestParam Long roomId, String locdate, Long scheduleId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return scheduleService.deleteSchedule(scheduleId, roomId, locdate, userDetails.getUser());
    }
}
