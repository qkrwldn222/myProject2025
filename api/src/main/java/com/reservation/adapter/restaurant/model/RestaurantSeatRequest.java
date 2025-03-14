package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantSeatRequest {

  @Schema(
      description = "좌석 타입",
      example = "TABLE",
      allowableValues = {"TABLE", "BAR", "PRIVATE"})
  private String seatType;

  @Schema(description = "좌석 번호", example = "A1")
  private String seatNumber;

  @Schema(description = "최대 수용 인원", example = "4")
  private Integer maxCapacity;

  @Schema(description = "사용 가능 여부", example = "true")
  private Boolean isAvailable;
}
