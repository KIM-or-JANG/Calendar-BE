package com.example.calendar.controller;

import com.example.calendar.global.util.Message;
import com.example.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/kimandjang")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendar")
    public ResponseEntity<Message> getdate(@RequestParam("month") String month, @RequestParam("year") String year) throws IOException, ParserConfigurationException, SAXException {
        return calendarService.getdata(month, year);
    }
}
