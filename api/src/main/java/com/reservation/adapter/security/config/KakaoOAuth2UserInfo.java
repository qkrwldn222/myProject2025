package com.reservation.adapter.security.config;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {
  public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    System.out.println(attributes.get("id"));
    return String.valueOf(attributes.get("id")); // Kakao는 "id" 필드가 사용자 ID
  }

  @Override
  public String getName() {
    Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
    return properties != null ? (String) properties.get("nickname") : "Unknown";
  }

  @Override
  public String getEmail() {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    return kakaoAccount != null ? (String) kakaoAccount.get("email") : null;
  }

  @Override
  public String getProvider() {
    return "kakao";
  }
}
