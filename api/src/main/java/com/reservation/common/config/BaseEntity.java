package com.reservation.common.config;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
  @CreatedDate
  @Column(name = "create_at", updatable = false)
  private LocalDateTime createAt; // 생성 시점

  @LastModifiedDate
  @Column(name = "update_at")
  private LocalDateTime updateAt; // 수정 시점

  @CreatedBy
  @Column(name = "create_by", updatable = false)
  private String createBy; // 생성 유저

  @LastModifiedBy
  @Column(name = "update_by")
  private String updateBy; // 수정 유저
}
