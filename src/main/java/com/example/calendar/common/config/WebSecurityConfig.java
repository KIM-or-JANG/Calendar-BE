package com.example.calendar.common.config;

import com.example.calendar.common.security.jwt.JwtAuthFilter;
import com.example.calendar.common.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // resources 접근 허용 설정
//        return web -> web.ignoring()
//                .requestMatchers(PathRequest.toH2Console())  // H2 > MySQL 전환시 삭제
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//
//    }

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")
//                .allowedOrigins("https://kim-or-jang.shop")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
//                .allowedHeaders("Content-Type", "X-AUTH-TOKEN", "Authorization", "Authorization_Refresh", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials")
//                .allowCredentials(true)
//                .maxAge(3000);
//    }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 사전에 약속된 출처를 명시
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://kim-or-jang.shop"));
        // 본 요청에 허용할 HTTP method(예비 요청에 대한 응답 헤더에 추가됨)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // 본 요청에 허용할 HTTP header(예비 요청에 대한 응답 헤더에 추가됨)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Authorization_Refresh", "Cache-Control", "Content-Type"));
        // 특정 헤더를 클라이언트 측에서 사용할 수 있게 지정
        // 만약 지정하지 않는다면, Authorization 헤더 내의 토큰 값을 사용할 수 없음
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Authorization_Refresh"));
        configuration.addExposedHeader(JwtUtil.ACCESS_TOKEN);
        configuration.addExposedHeader(JwtUtil.REFRESH_TOKEN);
        //  Preflight 요청의 캐시를 유지할 시간을 설정합니다. 여기서는 1800초(30분)로 설정되어 있습니다.
        configuration.setMaxAge(1800L);
        // 기본적으로 브라우저에서 인증 관련 정보들을 요청 헤더에 담지 않음
        // 이 설정을 통해서 브라우저에서 인증 관련 정보들을 요청 헤더에 담을 수 있도록 해줍니다.
        configuration.setAllowCredentials(true);
        // allowCredentials 를 true로 하였을 때,
        // allowedOrigin의 값이 * (즉, 모두 허용)이 설정될 수 없도록 검증합니다.
        configuration.validateAllowCredentials();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors((cors) -> cors.configurationSource(configurationSource()));
        http.csrf((csrf) -> csrf.disable());
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .requestMatchers("/").permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/api/**")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/api/user/kakao/callback")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"api/user/naver/callback")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/api/user/**")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET,"/api/token/**")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET, "/kimandjang/test")).permitAll()
                .requestMatchers(antMatcher(HttpMethod.GET, "/asd")).permitAll()
                .requestMatchers(antMatcher("/error")).permitAll()
//                .requestMatchers(HttpMethod.GET).permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}