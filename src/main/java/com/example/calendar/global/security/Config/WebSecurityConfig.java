package com.example.calendar.global.security.Config;

import com.example.calendar.global.security.Jwt.JwtAuthFilter;
import com.example.calendar.global.security.Jwt.JwtUtil;
import com.example.calendar.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화 //관리자용 API를 위해 활용
public class WebSecurityConfig  {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtUtil jwtUtil; // JwtUtil 클래스의 인스턴스를 주입받음

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder를 사용한 비밀번호 인코딩을 위한 빈 생성
    }

    private static final String[] PERMIT_URL_ARRAY = {
//            "/kimandjang/calendar"
    };
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .and().ignoring().requestMatchers(PERMIT_URL_ARRAY);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        (csrfConfig) -> csrfConfig.disable() // CSRF 보호 비활성화
                )
                .headers(
                        (headerConfig) -> headerConfig.frameOptions((frameOptionsConfig) -> frameOptionsConfig.disable()) // 브라우저의 Frame 옵션 비활성화
                )
                .authorizeHttpRequests
                        ((authorizeRequests) -> authorizeRequests
//                                .requestMatchers(PathRequest.toH2Console()).permitAll() // H2 데이터베이스 콘솔 접근을 모든 사용자에게 허용
//                                .requestMatchers(PERMIT_URL_ARRAY).permitAll() // 루트 및 로그인 페이지 접근을 모든 사용자에게 허용
//                                .requestMatchers("/posts/**", "/api/v1/posts/**").hasRole(Role.USER.name()) // 특정 경로에 대한 접근 권한 설정
//                                .requestMatchers("/admins/**", "/api/v1/admins/**").hasRole(Role.ADMIN.name()) // 특정 경로에 대한 접근 권한 설정
                                .anyRequest().authenticated()// 나머지 모든 요청은 인증 필요
                        )
                .oauth2Login(oauth2 -> oauth2                              //Oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                )
                .exceptionHandling(
                        (exceptionConfig) -> exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint) // 인증 실패 시 처리를 위한 핸들러 지정
                                .accessDeniedHandler(accessDeniedHandler) // 권한 없음 시 처리를 위한 핸들러 지정
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build(); // 보안 필터 체인 설정 반환
    }


    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Not Token in Request");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN, "Admin Error");
                response.setStatus(HttpStatus.FORBIDDEN.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    @Getter
    @RequiredArgsConstructor
    public class ErrorResponse {
        private final HttpStatus status; // HTTP 응답 상태 코드
        private final String message; // 오류 메시지
    }
}
