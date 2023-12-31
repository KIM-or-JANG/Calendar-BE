package com.example.calendar.user.social_Login.google.controller;

import com.example.calendar.common.util.Message;
import com.example.calendar.user.social_Login.google.service.NaverUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NaverController {
    private final NaverUserService naverUserService;

    @GetMapping("api/user/naver/callback")
    public ResponseEntity<Message> naverLogin(@RequestParam String code, String state, HttpServletResponse response) throws JsonProcessingException {
        return naverUserService.naverLogin(code, state, response);
    }
}
