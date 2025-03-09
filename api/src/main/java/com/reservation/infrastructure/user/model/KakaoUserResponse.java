package com.reservation.infrastructure.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class KakaoUserResponse {
  private Long id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  @Getter
  @ToString
  public static class KakaoAccount {
    private Profile profile;
  }

  @Getter
  @ToString
  public static class Profile {
    private String nickname;
  }
}
