package com.reservation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant_images", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantImage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long imageId;

  @ManyToOne
  @JoinColumn(name = "restaurant_id", nullable = false)
  private Restaurant restaurant;

  @Column(nullable = false, length = 500)
  private String imageUrl;

  @Column(nullable = false)
  private String imageType; // ex) "image/jpeg" or "image/png"

  @Column(nullable = false)
  private Boolean isThumbnail;
}
