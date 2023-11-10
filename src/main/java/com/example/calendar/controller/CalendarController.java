package com.example.calendar.controller;

import com.example.calendar.global.util.Message;
import com.example.calendar.global.security.UserDetails.UserDetailsImpl;
import com.example.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/kimandjang")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    //헤딩 딜의 데이터
//    @Secured("ROLE_ADMIN") //관리자용 API
    @GetMapping("/calendar")
    public ResponseEntity<Message> getMonthDate(
                                           @RequestParam("month") String month, @RequestParam("year") String year,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails ) throws IOException, ParserConfigurationException, SAXException {
        return calendarService.getMonthData(month, year, userDetails);
    }
}
