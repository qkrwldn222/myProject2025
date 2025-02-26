package com.reservation.adapter.security.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Environment environment;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 X
                .authorizeHttpRequests(auth -> {
                    // Swagger & OpenAPI 허용
                    auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll();

                    // 회원가입 & 로그인 허용
                    auth.requestMatchers("/api/auth/signup", "/api/auth/login").permitAll();

                    // Local or dev 환경에서만 임시 토큰 발급 허용
                    if (Arrays.asList(environment.getActiveProfiles()).contains("local") || Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                        auth.requestMatchers("/api/auth/temp/token").permitAll();
                    } else {
                        auth.requestMatchers("/api/auth/temp/token").denyAll();
                    }

                    // JWT 토큰 기반 Role별 접근 제어
                    auth.requestMatchers("/store/**").hasAnyAuthority("ROLE_00" , "ROLE_02"); // 00: Admin 만 가능
//                    auth.requestMatchers("/user/**").hasAnyAuthority("ROLE_00", "ROLE_01"); // 01: 일반 유저
//                    auth.requestMatchers("/owner/**").hasAnyAuthority("ROLE_00", "ROLE_02"); // 02: 가게 운영자
//                    auth.requestMatchers("/special/**").hasAuthority("ROLE_03"); // 03: 특정 API 접근 가능

                    // 그 외 모든 요청은 인증 필요
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
