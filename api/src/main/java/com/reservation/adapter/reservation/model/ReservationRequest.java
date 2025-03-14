package com.reservation.adapter.reservation.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "예약 요청")
public class ReservationRequest {
  @Schema(description = "레스토랑 ID", example = "10")
  private Long restaurantId;

  @Schema(description = "사용자 ID", example = "2")
  private Long userId;

  @Schema(description = "좌석 ID", example = "5")
  private Long seatId;

  @Schema(description = "예약 날짜 (yyyy-MM-dd)", example = "2024-06-20")
  private String reservationDate;

  @Schema(description = "예약 시간 (HH:mm)", example = "19:00")
  private String reservationTime;
}
