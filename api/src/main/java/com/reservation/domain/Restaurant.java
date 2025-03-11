package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "service.restaurant")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, length = 255)
    private String address;

    @OneToOne()
    @JoinColumn(name = "owner_id")
    private User ownerUser;

    @Column(name = "auto_confirm", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean autoConfirm; // 자동 승인 여부 (기본값: FALSE)

    @Column(name = "approval_timeout", nullable = false, columnDefinition = "INT DEFAULT 60")
    private Integer approvalTimeout; // 관리자가 승인하지 않으면 자동 취소 시간(분) (기본값: 60)

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_open_type", nullable = false, columnDefinition = "ENUM('ANYTIME', 'WEEKLY', 'MONTHLY') DEFAULT 'ANYTIME'")
    private ReservationOpenType reservationOpenType; // 예약 신청 가능 유형

    @Lob
    @Column(name = "reservation_open_days", columnDefinition = "TEXT DEFAULT NULL")
    private String reservationOpenDays; // 예약 신청 가능 요일/날짜 (JSON)

    @Lob
    @Column(name = "reservation_available_days", columnDefinition = "TEXT DEFAULT NULL")
    private String reservationAvailableDays; // 예약 가능한 날짜 범위 (JSON)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RestaurantStatus status; // PENDING, APPROVED, REJECTED

}