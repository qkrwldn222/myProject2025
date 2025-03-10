package com.reservation.infrastructure.user.repository;

import com.reservation.application.user.repository.UserKakaoRepository;
import com.reservation.common.config.ApiException;
import com.reservation.domain.KakaoUserInfo;
import com.reservation.infrastructure.user.model.KakaoTokenResponse;
import com.reservation.infrastructure.user.model.KakaoUserResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Repository
@RequiredArgsConstructor
public class UserKakaoExternalRepository implements UserKakaoRepository {
  private static final Logger logger = LoggerFactory.getLogger(UserKakaoExternalRepository.class);

  private final RestTemplate restTemplate;

  @Value("${interface.kakao.token-uri}")
  private String KAKAO_TOKEN_URL;

  @Value("${interface.kakao.user-info-uri}")
  private String KAKAO_USERINFO_URL;

  @Value("${interface.kakao.client-id}")
  private String CLIENT_ID;

  @Value("${interface.kakao.redirect-uri}")
  private String REDIRECT_URI;

  /**
   * 카카오 액세스 토큰 요청
   *
   * @param code 카카오 인증코드
   * @return String 카카오 액세스 토큰
   */
  @Override
  public String getKakaoAccessToken(String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("client_id", CLIENT_ID);
    params.add("redirect_uri", REDIRECT_URI);
    params.add("code", code);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

    logger.info("카카오 액세스 토큰 요청: {}", request);

    ResponseEntity<KakaoTokenResponse> response;
    try {
      response =
          restTemplate.exchange(
              KAKAO_TOKEN_URL, HttpMethod.POST, request, KakaoTokenResponse.class);
    } catch (Exception e) {
      logger.error("카카오 토큰 요청 실패", e);
      throw new ApiException("카카오 토큰 요청 실패: " + e.getMessage());
    }
    KakaoTokenResponse body =
        Optional.ofNullable(response.getBody())
            .orElseThrow(() -> new ApiException("카카오 액세스 토큰 응답이 없습니다."));

    logger.info("카카오 액세스 토큰 응답: {}", body);

    return body.getAccessToken();
  }

  /**
   * 카카오 사용자 정보 요청
   *
   * @param accessToken 인증토큰
   * @return KakaoUserInfo 카카오 인증정보 dto
   */
  @Override
  public KakaoUserInfo getKakaoUserInfo(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);

    HttpEntity<String> request = new HttpEntity<>(headers);

    logger.info("카카오 사용자 정보 요청: {}", request);

    ResponseEntity<KakaoUserResponse> response;
    try {
      response =
          restTemplate.exchange(
              KAKAO_USERINFO_URL, HttpMethod.GET, request, KakaoUserResponse.class);
    } catch (Exception e) {
      logger.error("카카오 사용자 정보 요청 실패", e);
      throw new ApiException("카카오 사용자 정보 요청 실패: " + e.getMessage());
    }

    KakaoUserResponse body =
        Optional.ofNullable(response.getBody())
            .orElseThrow(() -> new ApiException("카카오 사용자 정보 응답이 없습니다."));

    logger.info("카카오 사용자 정보 응답: {}", body);

    return KakaoUserInfo.builder()
        .id(body.getId())
        .nickname(body.getKakaoAccount().getProfile().getNickname())
        .build();
  }
}
