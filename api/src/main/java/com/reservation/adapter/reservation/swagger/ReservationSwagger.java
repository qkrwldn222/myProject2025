package com.reservation.adapter.reservation.swagger;

import com.reservation.adapter.reservation.model.ReservationRequest;
import com.reservation.adapter.reservation.model.RestaurantReservationResponse;
import com.reservation.common.enums.ReservationStatus;
import com.reservation.common.response.ApiErrorResponse;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "예약 관리", description = "예약 신청 및 취소 관리 API")
@RequestMapping("reservations")
public interface ReservationSwagger {

  @Operation(
      summary = "예약 생성",
      description = "레스토랑에 예약을 생성합니다.",
      security = @SecurityRequirement(name = "BearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "예약 생성 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value = "{\"message\":\"예약 생성 성공\",\"state\":\"SUCCESS\",\"id\": 1}"))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 "),
        @ApiResponse(responseCode = "403", description = "권한 없음 "),
        @ApiResponse(responseCode = "404", description = "가게를 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping("/reservation")
  ResponseEntity<ApiResponseWrapper<Long>> createReservation(
      @RequestBody ReservationRequest request);

  @Operation(
      summary = "예약 취소",
      description = "생성된 예약을 취소합니다.",
      responses = {
        @ApiResponse(responseCode = "200", description = "예약 취소 성공"),
        @ApiResponse(responseCode = "400", description = "예약 취소 불가능 (시간 초과 등)")
      })
  @DeleteMapping("/{reservationId}")
  ResponseEntity<ApiResponseWrapper<Void>> cancelReservation(
      @PathVariable("reservationId") Long reservationId);

  @Operation(
      summary = "예약 승인 처리",
      description = "관리자 또는 가게 운영자가 예약 요청을 승인합니다. 레스토랑 설정에 따라 자동 승인될 수 있습니다.",
      security = @SecurityRequirement(name = "BearerAuth"),
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "예약 승인 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                        {
                          "message": "예약 승인이 완료되었습니다.",
                          "state": "SUCCESS"
                        }
                    """))),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                        {
                          "message": "예약을 승인할 권한이 없습니다.",
                          "statusCode": 403
                        }
                    """))),
        @ApiResponse(
            responseCode = "404",
            description = "예약을 찾을 수 없음",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                        {
                          "message": "해당 예약 ID를 찾을 수 없습니다.",
                          "statusCode": 404
                        }
                    """))),
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                        {
                          "message": "서버 내부 오류가 발생했습니다.",
                          "statusCode": 500
                        }
                    """)))
      })
  @PostMapping("/{reservationId}/confirm")
  ResponseEntity<ApiResponseWrapper<Void>> confirmReservation(
      @PathVariable("reservationId") Long reservationId);

  @Operation(
      summary = "예약 내역 조회",
      description = "권한에 따른 예약 내역 조회 (일반 사용자는 자신, 가게 운영자는 자신의 가게, 관리자는 전체가 가능)",
      security = @SecurityRequirement(name = "BearerAuth"),
      parameters = {
        @Parameter(name = "userId", description = "유저id", example = "jiwoo9495"),
        @Parameter(name = "restaurant_id", description = "가게 ID", example = "1"),
        @Parameter(
            name = "reservationStatus",
            description = "예약 상태",
            example = "CONFIRM/PENDING/REJECT"),
        @Parameter(
            name = "reservationStartDate",
            description = "예약 조회 시작일",
            example = "2024-05-05"),
        @Parameter(name = "reservationEndDate", description = "예약 조회 종료일", example = "2024-05-05")
      },
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RestaurantReservationResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                        {
                          "message": "예약 내역이 조회되었습니다.",
                          "state": "SUCCESS"
                        }
                    """))),
        @ApiResponse(
            responseCode = "403",
            description = "권한 없음",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                        {
                          "message": "조회 권한이 없습니다.",
                          "statusCode": 403
                        }
                    """))),
        @ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                        {
                          "message": "서버 내부 오류가 발생했습니다.",
                          "statusCode": 500
                        }
                    """)))
      })
  @GetMapping("/search")
  ResponseEntity<ApiListResponse<RestaurantReservationResponse>> searchReservations(
      @RequestParam("userId") String userId,
      @RequestParam("restaurant_id") Long restaurant_id,
      @RequestParam("reservationStatus") ReservationStatus reservationStatus,
      @RequestParam("reservationStartDate") LocalDate reservationStarDate,
      @RequestParam("reservationEndDate") LocalDate reservationEndDate);
}
