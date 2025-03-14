package com.reservation.adapter.restaurant.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "운영 시간 생성, 수정, 삭제 요청 객체")
public class RestaurantOperatingHoursOperateRequest {
  @Schema(description = "새로운 운영 시간 추가 요청 목록", example = "[{\"dayOfWeek\": \"MON\", \"openTime\": \"09:00\", \"closeTime\": \"21:00\", \"isHoliday\": false}]")
  private List<RestaurantOperatingHoursCreateRequest> createRequests;

  @Schema(description = "기존 운영 시간 수정 요청 목록", example = "[{\"operatingHoursId\": 5, \"dayOfWeek\": \"FRI\", \"openTime\": \"10:00\", \"closeTime\": \"23:00\", \"isHoliday\": false}]")
  private List<RestaurantOperatingHoursUpdateRequest> updateRequests;

  @Schema(description = "삭제할 운영 시간 ID 목록", example = "[2, 4, 6]")
  private List<Long> deleteRequests;
}
