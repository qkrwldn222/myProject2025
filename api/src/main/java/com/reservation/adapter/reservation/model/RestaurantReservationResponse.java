package com.reservation.adapter.reservation.model;

import com.reservation.common.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantReservationResponse {
  @Schema(description = "예약 ID", example = "123")
  private Long reservationId;

  @Schema(description = "레스토랑 ID", example = "10")
  private Long restaurantId;

  @Schema(description = "레스토랑 이름", example = "맛있는 돈가스")
  private String restaurantName;

  @Schema(description = "레스토랑 카테고리", example = "한식")
  private String restaurantCategory;

  @Schema(description = "레스토랑 전화번호", example = "02-1234-5678")
  private String restaurantPhoneNumber;

  @Schema(description = "레스토랑 주소", example = "서울시 강남구 역삼동")
  private String restaurantAddress;

  @Schema(description = "사용자 ID", example = "20")
  private Long userId;

  @Schema(description = "좌석 ID", example = "5")
  private Long seatId;

  @Schema(description = "예약 일자", example = "2024-04-10")
  private LocalDate reservationDate;

  @Schema(description = "예약 시간", example = "12:00")
  private LocalTime reservationTime;

  @Schema(description = "예약 상태", example = "CONFIRMED")
  private ReservationStatus reservationStatus;

  @Schema(description = "예약금", example = "5000")
  private BigDecimal depositAmount;

  @Schema(description = "결제 키")
  private String paymentKey;

  @Schema(description = "주문 ID")
  private String orderId;

  @Schema(description = "예약 생성 일시")
  private LocalDateTime reservationAt;
}
