package com.example.calendar;

import com.example.calendar.common.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class asdController {
    @GetMapping("/asd")
    public ResponseEntity<Message> asd ()
             {
        return new ResponseEntity<>(new Message("테스트 성공",null), HttpStatus.OK);
    }
}
