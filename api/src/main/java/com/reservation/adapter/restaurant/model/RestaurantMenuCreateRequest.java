package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "메뉴 생성 요청 객체")
public class RestaurantMenuCreateRequest {
  @Schema(description = "메뉴 이름", example = "마르게리따 피자")
  private String name;

  @Schema(description = "메뉴 설명", example = "토마토, 바질, 모짜렐라 치즈가 들어간 전통 피자")
  private String description;

  @Schema(description = "메뉴 가격", example = "15000")
  private BigDecimal price;

  @Schema(description = "메뉴 이미지 URL", example = "https://example.com/pizza.jpg")
  private String imageUrl;

  @Schema(description = "대표 메뉴 여부 (true이면 인기 메뉴)", example = "true")
  private Boolean isBest;
}
