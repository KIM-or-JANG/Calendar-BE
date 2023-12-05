package com.example.calendar.hoilyDay.controller;

import com.example.calendar.common.security.userDetails.UserDetailsImpl;
import com.example.calendar.common.util.Message;
import com.example.calendar.scheadule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/kimandjang")
@RequiredArgsConstructor
public class HoliDayController {

    private final ScheduleService calendarService;
    @GetMapping("/test")
    public ResponseEntity<Message> test(
            @RequestParam("month") String month, @RequestParam("year") String year,
            @AuthenticationPrincipal UserDetailsImpl userDetails ) throws IOException, ParserConfigurationException, SAXException {
        return calendarService.test(month, year, userDetails);
    }
}
