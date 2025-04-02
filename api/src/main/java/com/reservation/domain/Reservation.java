package com.reservation.domain;

import com.reservation.common.enums.ReservationStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "restaurant_reservation", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private Long reservationId;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @JoinColumn(name = "seat_id", nullable = false)
  private RestaurantSeat seat;

  @Column(name = "reservation_date", nullable = false)
  private LocalDate reservationDate;

  @Column(name = "reservation_time", nullable = false)
  private LocalTime reservationTime;

  @Column(name = "reservation_end_time", nullable = false)
  private LocalTime reservationEndTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReservationStatus status;

  @Setter
  @Column(name = "expires_at")
  private LocalDateTime expiresAt;

  @Column(name = "deposit_amount")
  private BigDecimal depositAmount;

  @Column(name = "payment_key")
  private String paymentKey;

  @Column(name = "order_id")
  private String orderId;

  @Column(name = "reservation_at", updatable = false)
  @CreationTimestamp
  private LocalDateTime reservationAt;

  @Column(name = "cancelled_at")
  private LocalDateTime cancelledAt;

  @Column(name = "refund_amount")
  private BigDecimal refundAmount;

  public void cancel(BigDecimal refundAmount) {
    this.status = ReservationStatus.CANCELED;
    this.cancelledAt = LocalDateTime.now();
    this.refundAmount = refundAmount;
  }

  public void confirm() {
    this.status = ReservationStatus.CONFIRMED;
    this.expiresAt = null;
  }
}
