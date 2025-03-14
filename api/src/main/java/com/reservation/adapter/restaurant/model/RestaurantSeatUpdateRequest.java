package com.reservation.adapter.restaurant.model;

import com.reservation.common.enums.SeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantSeatUpdateRequest {
  @Schema(name = "seatId", description = "id")
  private Long seatId;

  @Schema(name = "seatType", description = "좌석 종류", example = "TABLE")
  private SeatType seatType;

  @Schema(name = "seatNumber", description = "좌석 번호", example = "5")
  private String seatNumber;

  @Schema(name = "maxCapacity", description = "최대 수용인원", example = "5")
  private int maxCapacity;

  @Schema(name = "isAvailable", description = "사용 가능여부", example = "true")
  private boolean isAvailable;
}
