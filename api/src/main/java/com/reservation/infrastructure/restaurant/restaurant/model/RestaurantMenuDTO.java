package com.reservation.infrastructure.restaurant.restaurant.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantMenuDTO {
  private Long menuId;
  private Long restaurantId;
  private String name;
  private String description;
  private BigDecimal price;
  private String imageUrl;
  private Boolean isBest;
}
