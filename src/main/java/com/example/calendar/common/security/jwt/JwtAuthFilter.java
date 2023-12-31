package com.example.calendar.common.security.jwt;

import com.example.calendar.common.security.SecurityExceptionDto;
import com.example.calendar.common.security.jwt.refreshToken.RefreshToken;
import com.example.calendar.common.security.jwt.refreshToken.RefreshTokenRepository;
import com.example.calendar.user.entity.User;
import com.example.calendar.user.entity.UserRoleEnum;
import com.example.calendar.common.exception.CustomException;
import com.example.calendar.common.exception.ErrorCode;
import com.example.calendar.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // JWT 토큰을 해석하여 추출
        String accessToken = jwtUtil.resolveToken(request, JwtUtil.ACCESS_TOKEN);
        String refreshToken = jwtUtil.resolveToken(request, JwtUtil.REFRESH_TOKEN);

        // 토큰이 존재하면 유효성 검사를 수행하고, 유효하지 않은 경우 예외 처리
        if (accessToken == null) {
            filterChain.doFilter(request, response);    //카카오 로그인 오류 부분 42번째 라인
        } else {
            if (jwtUtil.validateToken(accessToken)) {
                try {
                    setAuthentication(jwtUtil.getUserInfoFromToken(accessToken));
                } catch (UsernameNotFoundException e) {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
            } else if (refreshToken != null && jwtUtil.refreshTokenValid(refreshToken)) {
                //Refresh 토큰으로 유저명 가져오기
                String userEmail = jwtUtil.getUserInfoFromToken(refreshToken);
                //유저명으로 유저 정보 가져오기
                User user = userRepository.findByEmail(userEmail).orElseThrow(
                        () -> new CustomException(ErrorCode.USER_NOT_FOUND));
                //새로운 ACCESS TOKEN 발급
                log.info("===== 새로운 Access Token");
                String newAccessToken = jwtUtil.createToken(user.getEmail(), "Access", user.getId(), UserRoleEnum.USER);
                //Header 에 ACCESS TOKEN 추가
                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                setAuthentication(userEmail);

                // Refresh Token 재발급 로직, Header 에 REFRESHTOKEN 추가
                log.info("===== Create New Refresh Token");
                long refreshTime = jwtUtil.getExpirationTime(refreshToken);
                String newRefreshToken = jwtUtil.createNewRefreshToken(user.getEmail(), refreshTime, user.getId(), UserRoleEnum.USER);
                //-----새로운 RefreshToken저장
//                Optional<RefreshToken> getRefreshToken = refreshTokenRepository.findByEmail(user.getEmail());
//                refreshTokenRepository.save(getRefreshToken.get().updateToken(newRefreshToken));
                //-----
                jwtUtil.setHeaderRefreshToken(response, newRefreshToken);
            } else if (refreshToken == null) {
                jwtExceptionHandler(response, "AccessToken 이 만료되었습니다.", HttpStatus.BAD_REQUEST.value());
                return;
            } else {
                jwtExceptionHandler(response, "RefreshToken 이 만료되었습니다. 다시 로그인 해주세요.", HttpStatus.BAD_REQUEST.value());
                return;
            }
            // 다음 필터로 요청과 응답을 전달하여 필터 체인 계속 실행
            filterChain.doFilter(request, response);
        }
    }

    // 인증 객체를 생성하여 SecurityContext 에 설정
    public void setAuthentication(String userEmail) throws UsernameNotFoundException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(userEmail);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // JWT 예외 처리를 위한 응답 설정
    public void jwtExceptionHandler(HttpServletResponse response, String message, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json; charset=utf8"); //한국어 깨짐 문제 해결
        try {
            // 예외 정보를 JSON 형태로 변환하여 응답에 작성
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, message));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}