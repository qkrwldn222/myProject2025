package com.reservation.adapter.security.config;

import java.util.Map;

public class OAuth2UserInfoFactory {
  public static OAuth2UserInfo getOAuth2UserInfo(
      String registrationId, Map<String, Object> attributes) {
    if (registrationId.equalsIgnoreCase("google")) {
      // @TODO google
      return null;
    } else if (registrationId.equalsIgnoreCase("kakao")) {
      return new KakaoOAuth2UserInfo(attributes);
    } else if (registrationId.equalsIgnoreCase("naver")) {
      // @TODO naver
      return null;
    } else {
      throw new IllegalArgumentException("지원하지 않는 OAuth 로그인 서비스입니다: " + registrationId);
    }
  }
}
