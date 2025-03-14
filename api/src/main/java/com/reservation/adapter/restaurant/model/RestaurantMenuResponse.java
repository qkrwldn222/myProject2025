package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RestaurantMenuResponse {

    @Schema(description = "메뉴 ID", example = "10")
    private Long menuId;

    @Schema(description = "메뉴 이름", example = "랍스터 구이")
    private String name;

    @Schema(description = "메뉴 설명", example = "신선한 랍스터를 직접 구워 제공합니다.")
    private String description;

    @Schema(description = "가격 (단위: 원)", example = "75000")
    private BigDecimal price;

    @Schema(description = "메뉴 이미지 URL", example = "https://example.com/lobster.jpg")
    private String imageUrl;

    @Schema(description = "베스트 메뉴 여부", example = "true")
    private Boolean isBest;
}
