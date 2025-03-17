package com.reservation.adapter.cache.controller;

import com.reservation.adapter.cache.swagger.CacheSwagger;
import com.reservation.adapter.security.config.JwtTokenProvider;
import com.reservation.application.code.service.CodeService;
import com.reservation.application.menu.service.MenuService;
import com.reservation.application.waiting.service.SseEmitterService;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.common.response.ApiResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
public class CacheRestController implements CacheSwagger {

  private final MenuService menuService;
  private final CodeService codeService;
  private final SseEmitterService sseEmitterService;
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public ResponseEntity<ApiResponseWrapper<Void>> clearMenuCache() {
    menuService.clearMenuCache();
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("메뉴 정보 캐시를 초기화 했습니다."));
  }

  @Override
  public ResponseEntity<ApiResponseWrapper<Void>> clearCodeCache() {
    codeService.clearMenuCache();
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("공통 코드 캐시를 초기화 했습니다."));
  }

  @Override
  public ResponseEntity<ApiResponseWrapper<Void>> clearAuthCache() {
    jwtTokenProvider.clearAllJwtTokens();
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("로그인 인증 정보 캐시를 초기화 했습니다."));
  }

  @Override
  public ResponseEntity<ApiResponseWrapper<Void>> clearReservationCache() {
    jwtTokenProvider.clearAllReservationLocks();
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("예약 인증 정보 캐시를 초기화 했습니다."));
  }

  @Override
  public ResponseEntity<ApiResponseWrapper<Void>> clearWaiting(@RequestParam("restaurantId") Long restaurantId) {
    // 레스토랑 기준 웨이팅 정보를 초기화
    sseEmitterService.clearEmittersByRestaurant(restaurantId);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("예약 인증 정보 캐시를 초기화 했습니다."));
  }
}
