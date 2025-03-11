package com.reservation.domain;

import com.reservation.common.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "service.restaurant_reservation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Setter
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

}
