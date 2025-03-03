package com.reservation.domain;

import com.reservation.common.config.RoleType;
import com.reservation.common.converter.RoleTypeConverter;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Entity
@Table(name = "roles", schema = "common")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String name;

  @Convert(converter = RoleTypeConverter.class)
  @Column(nullable = false, name = "code")
  private RoleType roleType;

  @Column(nullable = false)
  private int rank;

  public GrantedAuthority getGrantedAuthority() {
    return new SimpleGrantedAuthority(String.valueOf(this.rank));
  }
}
