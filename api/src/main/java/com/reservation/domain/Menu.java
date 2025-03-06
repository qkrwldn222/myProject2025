package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu", schema = "common")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class Menu extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String code;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id")
  private Role role; // 접근 가능한 role
}
