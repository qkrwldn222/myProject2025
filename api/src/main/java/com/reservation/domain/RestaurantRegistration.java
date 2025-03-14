package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import com.reservation.common.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Table(name = "restaurant_registration", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantRegistration extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "registration_id")
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

  @Column private String rejectedReason;

  public void updateStatus(RegistrationStatus newStatus, String rejectedReason) {
    this.status = newStatus;
    this.rejectedReason = Optional.ofNullable(rejectedReason).orElse("");
  }
}
