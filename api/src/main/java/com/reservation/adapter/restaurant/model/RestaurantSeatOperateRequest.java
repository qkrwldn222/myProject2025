package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

@Data
@Schema(description = "좌석 생성, 수정, 삭제 요청 객체")
public class RestaurantSeatOperateRequest {
  @Schema(
      description = "새로운 좌석 추가 요청 목록",
      example =
          "[{\"seatType\": \"TABLE\", \"seatNumber\": \"A1\", \"maxCapacity\": 4, \"isAvailable\": true}]")
  private List<RestaurantSeatCreateRequest> createRequests;

  @Schema(
      description = "기존 좌석 수정 요청 목록",
      example =
          "[{\"seatId\": 3, \"seatType\": \"PRIVATE\", \"seatNumber\": \"VIP1\", \"maxCapacity\": 10, \"isAvailable\": false}]")
  private List<RestaurantSeatUpdateRequest> updateRequests;

  @Schema(description = "삭제할 좌석 ID 목록", example = "[5, 7, 9]")
  private List<Long> deleteRequests;
}
