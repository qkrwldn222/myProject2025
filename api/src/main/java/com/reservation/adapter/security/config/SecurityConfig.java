package com.reservation.adapter.security.config;

import com.reservation.application.menu.service.MenuRestService;
import com.reservation.common.config.ApiException;
import com.reservation.domain.Menu;
import com.reservation.domain.Role;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final Environment environment;
  private final MenuRestService menuRestService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 관리 X
        .securityContext(securityContext -> securityContext.requireExplicitSave(false))
        .authorizeHttpRequests(
            auth -> {
              // Swagger & OpenAPI 허용
              auth.requestMatchers(
                      "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/favicon.ico")
                  .permitAll();
              auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
              // 회원가입 & 로그인 허용
              auth.requestMatchers(
                      "/auth/signup", "/auth/login", "/auth/kakao/signup", "/auth/logout")
                  .permitAll();

              // Local or dev 환경에서만 임시 토큰 발급 허용
              if (Arrays.asList(environment.getActiveProfiles()).contains("local")
                  || Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
                auth.requestMatchers("/auth/temp/token").permitAll();
              } else {
                auth.requestMatchers("/auth/temp/token").denyAll();
              }

              auth.requestMatchers(
                      request -> {
                        Authentication authentication =
                            SecurityContextHolder.getContext().getAuthentication();
                        // 토큰의 사용자 권한을 가져 옴
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
                            boolean hasPermission = roleRank <= menuRole.getRank();
                            if (!hasPermission) {
                              throw new ApiException("접근 권한이 없습니다.");
                            }
                            return true;
                          }
                        }
                        throw new ApiException("접근 권한이 없습니다.");
                      })
                  .authenticated();

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
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  private String getUserRoleRankFromSecurityContext() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 인증 정보가 없거나, 익명 인증 (AnonymousAuthenticationToken)인 경우 null 반환
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return null;
    }

    // 권한이 없는 경우 null 반환
    return authentication.getAuthorities().stream()
        .findFirst() // 첫 번째 권한을 가져옴
        .map(GrantedAuthority::getAuthority) // 권한 문자열을 가져옴
        .orElse(null);
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
