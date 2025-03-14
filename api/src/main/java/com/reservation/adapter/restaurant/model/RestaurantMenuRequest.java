package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantMenuRequest {

  @Schema(description = "메뉴명", example = "돈가스")
  private String name;

  @Schema(description = "메뉴 설명", example = "수제 돈가스입니다.")
  private String description;

  @Schema(description = "가격", example = "12000")
  private BigDecimal price;

  @Schema(description = "메뉴 이미지 URL", example = "https://example.com/menu.jpg")
  private String imageUrl;

  @Schema(description = "추천 메뉴 여부", example = "true")
  private Boolean isBest;
}
