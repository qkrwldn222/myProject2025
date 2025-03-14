package com.reservation.adapter.restaurant.swagger;

import com.reservation.adapter.restaurant.model.*;
import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.RestaurantStatus;
import com.reservation.common.response.ApiErrorResponse;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "가게 관리", description = "가게 등록, 수정, 삭제 API")
@RequestMapping("/restaurants")
public interface RestaurantSwagger {

  @Operation(
      summary = "가게 등록",
      description = "가게 정보를 한 번에 입력하여 등록합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "가게 등록 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @PostMapping("/operate")
  ResponseEntity<ApiResponseWrapper<Long>> createRestaurant(
      @RequestBody RestaurantCreateRequest request);

  @Operation(
      summary = "가게 정보 수정",
      description = "가게의 기본 정보를 수정합니다.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "가게 정보 수정 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @PutMapping("/{restaurantId}")
  ResponseEntity<ApiResponseWrapper<Void>> updateRestaurant(
      @PathVariable("restaurantId") Long restaurantId,
      @RequestBody RestaurantUpdateRequest request);

  @Operation(
      summary = "가게 삭제 (운영 상태 변경)",
      description = "가게를 삭제하는 것이 아니라 운영 상태를 변경합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "가게 삭제(운영 상태 변경) 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @DeleteMapping("/{restaurantId}")
  ResponseEntity<ApiResponseWrapper<Void>> deleteRestaurant(
      @PathVariable("restaurantId") Long restaurantId);

  @Operation(
      summary = "좌석 일괄 등록/수정/삭제",
      description = "좌석을 한 번에 추가, 수정, 삭제할 수 있습니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "좌석 일괄 등록/수정/삭제 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @PostMapping("/{restaurantId}/seat/operate")
  ResponseEntity<ApiResponseWrapper<Void>> operateSeats(
      @PathVariable("restaurantId") Long restaurantId,
      @RequestBody RestaurantSeatOperateRequest request);

  @Operation(
      summary = "레스토랑 메뉴 관리",
      description = "메뉴를 추가, 수정, 삭제를 한 번의 요청으로 처리합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "메뉴 변경 성공",
            content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("/{restaurantId}/menu/operate")
  ResponseEntity<ApiResponseWrapper<Void>> operateMenu(
      @PathVariable("restaurantId") Long restaurantId,
      @RequestBody RestaurantMenuOperateRequest request);

  @Operation(
      summary = "레스토랑 운영 시간 관리",
      description = "운영 시간을 추가, 수정, 삭제를 한 번의 요청으로 처리합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "운영 시간 변경 성공",
            content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("/{restaurantId}/operating-Hours/operate")
  ResponseEntity<ApiResponseWrapper<Void>> operateOperatingHours(
      @PathVariable("restaurantId") Long restaurantId,
      @RequestBody RestaurantOperatingHoursOperateRequest request);

  @Operation(
      summary = "가게 입점 신청 조회",
      description = "가게 입점 신청 정보를 조회합니다.",
      security = @SecurityRequirement(name = "BearerAuth"),
      parameters = {
        @Parameter(name = "userId", description = "사용자 ID", example = "123"),
        @Parameter(name = "name", description = "가게 이름", example = "해운대 맛집"),
        @Parameter(name = "status", description = "가게 상태", example = "PENDING"),
        @Parameter(name = "managementStatus", description = "관리 상태", example = "APPROVED")
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "가게 목록 조회 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(implementation = RestaurantSearchRegistrationsResponse.class))),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class))),
        @ApiResponse(
            responseCode = "500",
            description = "서버 오류",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class)))
      })
  @GetMapping("/search/registrations")
  ResponseEntity<ApiListResponse<RestaurantSearchRegistrationsResponse>>
      searchRestaurantRegistrations(
          @RequestParam(name = "userId", required = false) String userId,
          @RequestParam(name = "name", required = false) String name,
          @RequestParam(name = "status", required = false) RestaurantStatus status,
          @RequestParam(name = "managementStatus", required = false)
              RegistrationStatus managementStatus);

  @Operation(
      summary = "가게 입점 신청 처리 (승인/거절)",
      description = "관리자가 가게의 입점 신청을 승인하거나 거절합니다.",
      security = @SecurityRequirement(name = "BearerAuth"),
      parameters = {
        @Parameter(
            name = "state",
            description = "처리 상태 (APPROVED / REJECTED)",
            required = true,
            example = "APPROVED"),
        @Parameter(name = "rejectedReason", description = "사유", example = "맛이 없어보여요.")
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "입점 신청 처리 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (이미 승인됨/거절됨 또는 존재하지 않는 가게)"),
        @ApiResponse(responseCode = "403", description = "권한 없음 (관리자만 승인 가능)"),
        @ApiResponse(responseCode = "404", description = "가게 또는 신청 내역을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping("/{restaurantId}/process")
  ResponseEntity<ApiResponseWrapper<Void>> processRestaurantApproval(
      @PathVariable Long restaurantId,
      @RequestParam RegistrationStatus state,
      @RequestParam String rejectedReason);
}
