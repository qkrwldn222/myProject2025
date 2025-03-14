package com.reservation.domain;

import com.reservation.application.restaurant.restaurant.model.RestaurantSeatUpdateCommand;
import com.reservation.common.enums.SeatType;
import jakarta.persistence.*;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_seat", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantSeat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seatId;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SeatType seatType; // TABLE, BAR, PRIVATE

  @Column(nullable = false, length = 10)
  private String seatNumber;

  @Column(nullable = false)
  private Integer maxCapacity;

  @Column(nullable = false)
  private Boolean isAvailable;

  public void update(RestaurantSeatUpdateCommand command) {
    this.seatType = Optional.ofNullable(command.getSeatType()).orElse(this.seatType);
    this.seatNumber = Optional.ofNullable(command.getSeatNumber()).orElse(this.seatNumber);
    this.maxCapacity = Optional.ofNullable(command.getMaxCapacity()).orElse(this.maxCapacity);
    this.isAvailable = Optional.ofNullable(command.isAvailable()).orElse(this.isAvailable);
  }
}
