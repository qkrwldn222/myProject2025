package com.reservation.domain;

import com.reservation.application.restaurant.restaurant.model.RestaurantUpdateCommand;
import com.reservation.common.config.BaseEntity;
import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.RestaurantStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant", catalog = "service")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "restaurant_id")
  private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, length = 100)
  private String category;

  @Column(nullable = false, length = 20)
  private String phoneNumber;

  @Column(nullable = false, length = 255)
  private String address;

  @Column(name = "owner_id")
  private Long ownerId;

  @Column(name = "auto_confirm", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private Boolean autoConfirm; // 자동 승인 여부 (기본값: FALSE)

  @Column(name = "approval_timeout", nullable = false, columnDefinition = "INT DEFAULT 60")
  private Integer approvalTimeout; // 관리자가 승인하지 않으면 자동 취소 시간(분) (기본값: 60)

  @Enumerated(EnumType.STRING)
  @Column(
      name = "reservation_open_type",
      nullable = false,
      columnDefinition = "ENUM('ANYTIME', 'WEEKLY', 'MONTHLY') DEFAULT 'ANYTIME'")
  private ReservationOpenType reservationOpenType; // 예약 신청 가능 유형

  @Lob
  @Column(name = "reservation_open_days", columnDefinition = "TEXT DEFAULT NULL")
  private String reservationOpenDays; // 예약 신청 가능 요일/날짜 (JSON)

  @Lob
  @Column(name = "special_booking_days", columnDefinition = "TEXT DEFAULT NULL")
  private String specialBookingDays; // 예약 가능 날짜 (JSON 형태)

  @Lob
  @Column(name = "reservation_available_days", columnDefinition = "TEXT DEFAULT NULL")
  private String reservationAvailableDays; // 예약 가능한 날짜 범위 (JSON)

  @Column(name = "deposit_amount")
  private BigDecimal depositAmount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20,
          columnDefinition = "ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING'", name = "status")
  private RegistrationStatus status;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20,
          columnDefinition = "ENUM('OPEN', 'PAUSED','CLOSED') DEFAULT 'OPEN'" , name = "management_status")
  private RestaurantStatus managementStatus;

  /** 가게 영업 종료 변경 */
  public void markAsDeleted() {
    this.managementStatus = RestaurantStatus.CLOSED;
  }

  // 가게 정보 업데이트
  public void update(RestaurantUpdateCommand command) {
    this.name = Optional.ofNullable(command.getName()).orElse(this.name);
    this.category = Optional.ofNullable(command.getCategory()).orElse(this.category);
    this.phoneNumber =  Optional.ofNullable(command.getPhoneNumber()).orElse(this.phoneNumber) ;
    this.address =   Optional.ofNullable(command.getAddress()).orElse(this.address) ;
    this.autoConfirm = Optional.ofNullable(command.getAutoConfirm()).orElse(this.autoConfirm);
    this.approvalTimeout = Optional.ofNullable(command.getApprovalTimeout()).orElse(this.approvalTimeout);
    this.specialBookingDays = Optional.ofNullable(command.getSpecialBookingDays()).orElse(this.specialBookingDays);
    this.reservationOpenType = Optional.ofNullable(command.getReservationOpenType()).orElse(this.reservationOpenType);
    this.reservationOpenDays = Optional.ofNullable(command.getReservationOpenDays()).orElse(this.reservationOpenDays);
    this.reservationAvailableDays = Optional.ofNullable(command.getReservationAvailableDays()).orElse(this.reservationAvailableDays);
    this.depositAmount = Optional.ofNullable(command.getDepositAmount()).orElse(this.depositAmount);
    this.managementStatus = Optional.ofNullable(command.getRestaurantStatus()).orElse(this.managementStatus);
  }

  public void updateStatus(RegistrationStatus newStatus) {
    this.status = newStatus;
  }
}
