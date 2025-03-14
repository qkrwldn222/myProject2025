package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "메뉴 생성, 수정, 삭제 요청 객체")
public class RestaurantMenuOperateRequest {
  @Schema(
      description = "새로운 메뉴 추가 요청 목록",
      example =
          "[{\"name\": \"스테이크\", \"description\": \"숙성 스테이크\", \"price\": 35000, \"imageUrl\": \"https://example.com/steak.jpg\", \"isBest\": true}]")
  private List<RestaurantMenuCreateRequest> createRequests;

  @Schema(
      description = "기존 메뉴 수정 요청 목록",
      example =
          "[{\"menuId\": 1, \"name\": \"새우볶음밥\", \"description\": \"새우가 가득한 볶음밥\", \"price\": 12000, \"imageUrl\": \"https://example.com/shrimp_rice.jpg\", \"isBest\": false}]")
  private List<RestaurantMenuUpdateRequest> updateRequests;

  @Schema(description = "삭제할 메뉴 ID 목록", example = "[3, 5, 7]")
  private List<Long> deleteRequests;
}
