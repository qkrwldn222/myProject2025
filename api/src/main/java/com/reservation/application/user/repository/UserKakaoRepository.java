package com.reservation.application.user.repository;

import com.reservation.domain.KakaoUserInfo;

public interface UserKakaoRepository {
  String getKakaoAccessToken(String code);

  KakaoUserInfo getKakaoUserInfo(String accessToken);
}
