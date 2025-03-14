package com.reservation.adapter.restaurant.model;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "메뉴 수정 요청 객체")
public class RestaurantMenuUpdateRequest {
  @Schema(description = "메뉴 ID (수정할 메뉴의 고유 ID)", example = "10")
  private Long menuId;

  @Schema(description = "메뉴 이름", example = "고르곤졸라 피자")
  private String name;

  @Schema(description = "메뉴 설명", example = "고르곤졸라 치즈와 꿀을 곁들인 피자")
  private String description;

  @Schema(description = "메뉴 가격", example = "18000")
  private BigDecimal price;

  @Schema(description = "메뉴 이미지 URL", example = "https://example.com/gorgonzola.jpg")
  private String imageUrl;

  @Schema(description = "대표 메뉴 여부 (true이면 인기 메뉴)", example = "false")
  private Boolean isBest;
}
