package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import com.reservation.common.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service.restaurant_registration")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantRegistration extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationStatus status; // PENDING, APPROVED, REJECTED

    @Column
    private String rejectedReason;
}