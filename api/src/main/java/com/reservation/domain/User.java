package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users", schema = "common")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, name = "user_id")
  private String userID;

  @Column(nullable = false, name = "user_name")
  private String username;

  @Column private String password;

  @Column private String provider;

  @OneToOne
  @JoinColumn(name = "role_id")
  private Role role;
}
