package com.example.calendar.user.social_Login.google.service;

import com.example.calendar.common.security.jwt.JwtUtil;
import com.example.calendar.common.util.Message;
import com.example.calendar.user.entity.User;
import com.example.calendar.user.entity.UserRoleEnum;
import com.example.calendar.user.repository.UserRepository;
import com.example.calendar.user.social_Login.google.dto.NaverUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    public ResponseEntity<Message> naverLogin(String code, String state, HttpServletResponse response) throws JsonProcessingException {
        // 인가코드, state 로 네이버한테 access_token 요청
        String accessToken = getToken(code, state);

        // access_token 으로 사용자 정보 가져오기
        NaverUserInfoDto naverUserInfoDto = getNaverUserinfo(accessToken);

        // 회원가입
        User naverUser = registerNaverUser(naverUserInfoDto);

        // 토큰 헤더에 담기
        jwtUtil.createAndSetToken(response, naverUser.getEmail(), naverUser.getId());

        return new ResponseEntity<>(new Message("네이버 로그인", naverUserInfoDto.getNickName()),HttpStatus.OK);
    }
    //  https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=N67sIS7i6jSW96HgtEcW&redirect_uri=http://localhost:8080/api/user/naver/callback&state=state
    //  https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=N67sIS7i6jSW96HgtEcW&redirect_uri=https://kim-or-jang.shop/api/user/naver/callback&state=state
    private String getToken(String code, String state) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", naverClientId);
        body.add("client_secret", naverClientSecret);
        body.add("redirect_uri", naverRedirectUri);
//      body.add("redirect_uri", "http://localhost:8080/api/user/naver/callback&state=state");
        body.add("code", code);
        body.add("state", state);

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private NaverUserInfoDto getNaverUserinfo(String accessToken) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper2 = new ObjectMapper();
        JsonNode jsonNode2 = objectMapper2.readTree(responseBody).get("response");

        Long id = jsonNode2.get("id").asLong();
        String nickname = jsonNode2.get("name").asText();
        String email = jsonNode2.get("email").asText();
        String profileImage = jsonNode2.get("profile_image").asText();

        log.info("네이버 사용자 정보: " + id + ", " + nickname + ", " + email + ", " + profileImage);
        return new NaverUserInfoDto(id, nickname, email, profileImage);
    }

    @SneakyThrows
    private User registerNaverUser(NaverUserInfoDto naverUserInfoDto) {
        Long naverId = naverUserInfoDto.getId();
        String profileImage = naverUserInfoDto.getProfileImage();
        User naverUser = userRepository.findByNaverId(naverId).orElse(null);
        Random random = SecureRandom.getInstanceStrong();

        if (naverUser == null) {
            Long navernewId = random.nextLong();
            String naverEmail = naverUserInfoDto.getEmail();
            User sameEmailUser = userRepository.findByEmail(naverEmail).orElse(null);
            if (sameEmailUser != null) {
                naverUser = sameEmailUser;
                naverUser = naverUser.naverUpdate(navernewId, profileImage);
            } else {
                String password = UUID.randomUUID().toString();
                String encodePassword = passwordEncoder.encode(password);
                String email = naverUserInfoDto.getEmail();

                naverUser = new User(null, navernewId, null, email, naverUserInfoDto.getNickName(), encodePassword, profileImage, UserRoleEnum.USER);
            }
            userRepository.save(naverUser);
        }
        return naverUser;
    }
}

