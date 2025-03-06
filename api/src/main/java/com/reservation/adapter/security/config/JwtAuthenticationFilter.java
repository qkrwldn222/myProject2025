package com.reservation.adapter.security.config;

import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserDetailsService userDetailsService;
  private final UserService userService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    logger.info("Request URL : " + request.getRequestURL().toString());

    String token = resolveToken(request);
    if (token != null && jwtTokenProvider.validateToken(token)) {
      String userId = jwtTokenProvider.getUsernameFromToken(token);
      User user =
          userService
              .findByUserId(userId)
              .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다: " + userId));

      // CustomUserDetails 객체 생성
      CustomUserDetails userDetails = new CustomUserDetails(user);

      // SecurityContext에 인증 객체 저장
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    chain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
