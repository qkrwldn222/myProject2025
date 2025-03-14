package com.reservation.domain;

import com.reservation.application.restaurant.restaurant.model.RestaurantOperatingHoursUpdateCommand;
import com.reservation.common.enums.DayOfWeekEnum;
import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_operating_hours", catalog = "service")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantOperatingHours {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long operatingId;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DayOfWeekEnum dayOfWeek;

  @Column(nullable = false)
  private LocalTime openTime;

  @Column(nullable = false)
  private LocalTime closeTime;

  @Column(nullable = false)
  private Boolean isHoliday;

  public void update(RestaurantOperatingHoursUpdateCommand command) {
    this.dayOfWeek = command.getDayOfWeek();
    this.openTime = command.getOpenTime();
    this.closeTime = command.getCloseTime();
    this.isHoliday = command.getIsHoliday();
  }
}
