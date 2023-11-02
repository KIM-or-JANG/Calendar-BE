package com.example.calendar.service;

import com.example.calendar.global.HoilyDayApi.HoliyDay;
import com.example.calendar.global.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;


@Service
@RequiredArgsConstructor
public class CalendarService {
    private final HoliyDay holiyDay;

//    public ResponseEntity<Message> getdata(String month, String year) throws IOException {
//        HoilyDay hoilyDay = new HoilyDay();
//        return new ResponseEntity<>(new Message(null,hoilyDay.hoilydata(month, year)),HttpStatus.OK);
//    }

    public ResponseEntity<Message> getdata(String month, String year) throws IOException, ParserConfigurationException, SAXException {
        return new ResponseEntity<>(new Message(null,holiyDay.holiydata(month, year)),HttpStatus.OK);
    }

}

