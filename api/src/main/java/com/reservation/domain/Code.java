package com.reservation.domain;

import com.reservation.common.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "code", schema = "common")
public class Code extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", length = 20)
  private String code; // 공통 코드

  @Column(name = "name", length = 20)
  private String name; // 코드 명

  @Column(name = "attr01", length = 100)
  private String attr01; // 속성1

  @Column(name = "attr02", length = 100)
  private String attr02; // 속성2

  @Column(name = "attr03", length = 100)
  private String attr03; // 속성3

  @Column(name = "attr04", length = 100)
  private String attr04; // 속성4

  @Column(name = "attr05", length = 100)
  private String attr05; // 속성5

  @Column(name = "attr06", length = 100)
  private String attr06; // 속성6

  @Column(name = "attr07", length = 100)
  private String attr07; // 속성7

  @Column(name = "attr08", length = 100)
  private String attr08; // 속성8

  @Column(name = "attr09", length = 100)
  private String attr09; // 속성9

  @Column(name = "group_code", length = 100, nullable = false)
  private String groupCode; // 그룹 코드
}
