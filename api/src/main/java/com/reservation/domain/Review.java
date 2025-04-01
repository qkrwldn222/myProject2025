package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reviewId;

  @JoinColumn(name = "user_id", nullable = false)
  private Long userId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id", nullable = true)
  private Reservation reservation; // 예약 리뷰일 경우 참조

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "waiting_id", nullable = true)
  private WaitingHistory waitingHistory; // 웨이팅 리뷰일 경우 참조

  @Column(name = "review_text", nullable = false, length = 500)
  private String reviewText;

  @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ReviewImage> reviewImages = new ArrayList<>();
}
