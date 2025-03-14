package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "refund_policy", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundPolicy extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Column(name = "days_before", nullable = false)
  private int daysBefore;

  @Column(name = "refund_percentage", nullable = false)
  private BigDecimal refundPercentage;
}
