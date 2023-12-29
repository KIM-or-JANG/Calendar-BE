package com.example.calendar.user.social_Login.kakao.service;

import com.example.calendar.user.entity.User;
import com.example.calendar.user.entity.UserRoleEnum;
import com.example.calendar.common.security.jwt.JwtUtil;
import com.example.calendar.common.util.Message;
import com.example.calendar.user.repository.UserRepository;
import com.example.calendar.user.social_Login.kakao.dto.KakaoUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;
//    카카오로그인 URL
//    kauth.kakao.com/oauth/authorize?client_id=58fc709768dcd1b5dc6a4f72874b6e2b&redirect_uri=https://kim-or-jang.shop/api/user/kakao/callback&response_type=code
//    kauth.kakao.com/oauth/authorize?client_id=58fc709768dcd1b5dc6a4f72874b6e2b&redirect_uri=http://localhost:8080/api/user/kakao/callback&response_type=code
//    https://accounts.kakao.com/login/?continue=https%3A%2F%2Fkauth.kakao.com%2Foauth%2Fauthorize%3Fresponse_type%3Dcode%26redirect_uri%3Dhttps%253A%252F%252Fkim-or-jang.shop%252Fapi%252Fuser%252Fkakao%252Fcallback%26through_account%3Dtrue%26client_id%3D58fc709768dcd1b5dc6a4f72874b6e2b#webTalkLogin
    @Transactional
    public ResponseEntity<Message> kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);    //잘 안되면 try-catch로 오류 확인해보기

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 토큰 반환
        jwtUtil.createAndSetToken(response, kakaoUser.getEmail(), kakaoUser.getId());

        return new ResponseEntity<>(new Message("카카오 로그인 성공",null),HttpStatus.OK);
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청(토큰 받기)
    private String getToken(String code) throws JsonProcessingException {
        
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientId);
        body.add("client_secret", kakaoClientSecret);
        body.add("redirect_uri", kakaoRedirectUri);
//        body.add("redirect_uri", "http://localhost:8080/api/user/kakao/callback");
        body.add("code", code);
        

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        ResponseEntity<String> response = new RestTemplate().exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String accessToken = String.valueOf(jsonNode.get("access_token"));
        log.info("Access Token: {}", accessToken);
        return jsonNode.get("access_token").asText();
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String profileImage = jsonNode.get("properties")
                .get("profile_image").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email + ", " + profileImage);
        return new KakaoUserInfoDto(id, nickname, email, profileImage);
    }

    // 3. 필요시에 회원가입
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        String profileImage = "https://kim-or-jang-calendar-profile.s3.ap-northeast-2.amazonaws.com/user.png";
        if (kakaoUserInfo.getProfileImage() != null) profileImage = kakaoUserInfo.getProfileImage();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);
        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id, 프로필 이미지 추가
                kakaoUser = kakaoUser.kakaoUpdate(kakaoId, profileImage);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(kakaoId, null, null, email, kakaoUserInfo.getNickName(), encodedPassword, profileImage,UserRoleEnum.USER);
            }
            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}