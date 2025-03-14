package com.reservation.common.utils;

import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
  /**
   * 현재 인증된 사용자의 권한(Role) 반환
   *
   * @return 사용자의 첫 번째 권한(Role) 또는 null
   */
  public static String getCurrentUserRole() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return null;
    }

    return authentication.getAuthorities().stream()
        .findFirst() // 첫 번째 권한 가져오기
        .map(GrantedAuthority::getAuthority) // 권한 문자열 변환
        .orElse(null);
  }

  /**
   * 현재 인증된 사용자의 ID 반환 (UserDetails가 Principal일 경우)
   *
   * @return 사용자 ID 또는 null
   */
  public static String getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return null;
    }

    return Optional.ofNullable(authentication.getPrincipal())
        .map(Object::toString) // Principal을 문자열로 변환
        .orElse(null);
  }

  /**
   * 현재 사용자가 인증된 상태인지 확인
   *
   * @return 인증 여부 (true: 인증됨, false: 인증되지 않음)
   */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
  }
}
