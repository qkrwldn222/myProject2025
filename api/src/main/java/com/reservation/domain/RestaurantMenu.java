package com.reservation.domain;

import com.reservation.application.restaurant.restaurant.model.RestaurantMenuUpdateCommand;
import com.reservation.common.config.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;
import lombok.*;

@Entity
@Table(name = "restaurant_menu", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantMenu extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long menuId;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;

  @Column(length = 500)
  private String imageUrl;

  @Column(nullable = false)
  private Boolean isBest; // 대표 메뉴

  public void update(RestaurantMenuUpdateCommand command) {
    this.name = command.getName();
    this.description = Optional.ofNullable(command.getDescription()).orElse(this.description);
    this.price = Optional.ofNullable(command.getPrice()).orElse(this.price);
    this.imageUrl = Optional.ofNullable(command.getImageUrl()).orElse(this.imageUrl);
    this.isBest = Optional.ofNullable(command.getIsBest()).orElse(this.isBest);
  }
}
