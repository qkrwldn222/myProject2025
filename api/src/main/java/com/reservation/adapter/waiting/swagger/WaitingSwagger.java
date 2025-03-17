package com.reservation.adapter.waiting.swagger;

import com.reservation.common.response.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "웨이팅 시스템 API", description = "음식점별 웨이팅 시스템 관리")
@RequestMapping("waiting")
public interface WaitingSwagger {

    @Operation(summary = "웨이팅 대기열 참여", description = "사용자가 지정된 레스토랑의 대기열에 참여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기열에 성공적으로 추가됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "레스토랑을 찾을 수 없음")
    })
    @PostMapping("/join")
    ResponseEntity<ApiResponseWrapper<Void>> joinQueue(@RequestParam("restaurantId") Long restaurantId);

    @Operation(summary = "웨이팅 대기열 나가기", description = "사용자가 지정된 레스토랑의 대기열에서 나갑니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대기열에서 성공적으로 제거됨"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "레스토랑을 찾을 수 없음")
    })
    @PostMapping("/leave")
    ResponseEntity<ApiResponseWrapper<Void>> leaveQueue(@RequestParam Long restaurantId);

    @Operation(summary = "관리자가 입장 허용", description = "레스토랑 운영자가 다음 대기자를 입장시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다음 사용자가 입장 허용됨"),
            @ApiResponse(responseCode = "403", description = "운영자 권한 없음"),
            @ApiResponse(responseCode = "404", description = "레스토랑을 찾을 수 없음")
    })
    @PostMapping("/allow")
    ResponseEntity<ApiResponseWrapper<Void>> allowNextUser(@RequestParam Long restaurantId);

    @Operation(summary = "첫번째 대기열 입장", description = "레스토랑 운영자가 첫번째 대기열을 지웁니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다음 사용자가 입장 허용됨"),
            @ApiResponse(responseCode = "403", description = "운영자 권한 없음"),
            @ApiResponse(responseCode = "404", description = "레스토랑을 찾을 수 없음")
    })
    @PostMapping("/enter")
    ResponseEntity<ApiResponseWrapper<Void>> enterNextUser(@RequestParam("restaurantId") Long restaurantId);


    @Operation(summary = "SSE 구독", description = "사용자가 지정된 레스토랑의 웨이팅 이벤트를 실시간으로 구독합니다.")
    @GetMapping("/subscribe")
    SseEmitter subscribe(@RequestParam("restaurantId") Long restaurantId, @RequestParam("userId") String userId);
}
