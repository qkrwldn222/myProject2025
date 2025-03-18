package com.reservation.adapter.waiting.controller;

import com.reservation.adapter.waiting.swagger.WaitingSwagger;
import com.reservation.application.waiting.service.SseEmitterService;
import com.reservation.application.waiting.service.WaitingService;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.common.response.ApiResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController()
@RequestMapping("waiting")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
public class WaitingController implements WaitingSwagger {
  private final WaitingService waitingService;
  private final SseEmitterService sseEmitterService;

  @Override
  @PostMapping("/join")
  public ResponseEntity<ApiResponseWrapper<Void>> joinQueue(
      @RequestParam("restaurantId") Long restaurantId) {
    waitingService.joinWaitingQueue(restaurantId);
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("대기열에 추가 되었습니다."));
  }

  @Override
  @PostMapping("/leave")
  public ResponseEntity<ApiResponseWrapper<Void>> leaveQueue(
      @RequestParam("restaurantId") Long restaurantId) {
    waitingService.leaveWaitingQueue(restaurantId);
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("대기열에서 제거 되었습니다."));
  }

  @Override
  @PostMapping("/allow")
  public ResponseEntity<ApiResponseWrapper<Void>> allowNextUser(
      @RequestParam("restaurantId") Long restaurantId) {
    waitingService.allowNextUser(restaurantId);
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("다음 사용자 입장 허용 되었습니다."));
  }

  @Override
  @GetMapping("/subscribe")
  public SseEmitter subscribe(
      @RequestParam("restaurantId") Long restaurantId, @RequestParam("userId") String userId) {
    return sseEmitterService.subscribe(userId, restaurantId);
  }

  @Override
  @GetMapping("/enter")
  public ResponseEntity<ApiResponseWrapper<Void>> enterNextUser(
      @RequestParam("restaurantId") Long restaurantId) {
    waitingService.leaveWaitingQueueByOwner(restaurantId);
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("입장 완료되었습니다."));
  }
}
