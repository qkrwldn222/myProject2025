package com.reservation.adapter.security.config;


import com.reservation.application.menu.service.MenuRestService;
import com.reservation.domain.Menu;
import com.reservation.domain.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final Environment environment;
    private final MenuRestService menuRestService;

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


                    auth.requestMatchers(request -> {
                        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                        String role = getUserRoleRankFromSecurityContext();

                        System.out.println("현재 요청의 인증 객체: " + authentication);
                        if (authentication == null || !authentication.isAuthenticated()) {
                            return false;
                        }

                        if (!StringUtils.hasText(role)) {
                            return false;
                        }

                        int roleRank = Integer.parseInt(role);
                        String menuCode = extractMenuCode(request);

                        if (StringUtils.hasText(menuCode)) {
                            Optional<Menu> menu = menuRestService.findMenuByCode(menuCode);
                            if (menu.isPresent()) {
                                Role menuRole = menu.get().getRole();
                                return roleRank <= menuRole.getRank();
                            }
                        }
                        return false;
                    }).authenticated();

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

    private String getUserRoleRankFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                return authority.getAuthority(); // 첫 번째 역할 반환
            }
        }
        return null;
    }

    private String extractMenuCode(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] parts = requestURI.split("/");
        if (parts.length > 1) {
            return parts[1]; // 첫 번째 경로 반환 (예: /store/search -> store 반환)
        }
        return null;
    }

}
