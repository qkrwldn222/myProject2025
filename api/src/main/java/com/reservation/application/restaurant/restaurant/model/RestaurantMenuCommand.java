package com.reservation.application.restaurant.restaurant.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantMenuCommand {
  private String name;
  private String description;
  private BigDecimal price;
  private String imageUrl;
  private Boolean isBest;
}
