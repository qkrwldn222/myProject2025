package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "waiting_history", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long waitingId;

  @JoinColumn(name = "user_id", nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Column(name = "enter_time", nullable = false)
  private LocalDateTime enterTime;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime endTime;
}
