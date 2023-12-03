package com.example.calendar.social.kakao.controller;

import com.example.calendar.global.util.Message;
import com.example.calendar.social.kakao.service.KakaoUserService;
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
public class KakaoController {

    private final KakaoUserService kakaoService;
    @GetMapping("api/user/kakao/callback")
    public ResponseEntity<Message> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        // 리프레시 토큰을 받기 위해 tokenDto 생성
        return kakaoService.kakaoLogin(code,response);
    }
}
