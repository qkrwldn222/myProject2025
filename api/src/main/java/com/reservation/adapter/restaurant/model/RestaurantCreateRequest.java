package com.reservation.adapter.restaurant.model;

import com.reservation.common.enums.ReservationOpenType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantCreateRequest {
  @Schema(description = "가게명", example = "맛있는 돈가스")
  private String name;

  @Schema(description = "카테고리", example = "한식")
  private String category;

  @Schema(description = "연락처", example = "02-1234-5678")
  private String phoneNumber;

  @Schema(description = "주소", example = "서울시 강남구")
  private String address;

  @Schema(description = "운영자 ID")
  private Long ownerId;

  @Schema(description = "자동 승인 여부", example = "true")
  private Boolean autoConfirm;

  @Schema(description = "관리자 승인 제한 시간 (분)", example = "60")
  private Integer approvalTimeout;

  @Schema(
      description = "예약 가능 날짜 설정 (JSON)",
      example =
          "{\"type\": \"FIXED_RANGE\", \"start_date\": \"2024-04-01\", \"end_date\": \"2024-04-30\"}")
  private String specialBookingDays;

  @Schema(description = "예약 신청 유형", example = "ANYTIME, WEEKLY, MONTHLY")
  private ReservationOpenType reservationOpenType;

  @Schema(description = "예약 신청 가능 요일/날짜 (JSON)", example = "[\"MON\", \"TUE\"]")
  private String reservationOpenDays;

  @Schema(description = "예약 가능 날짜 설정 (JSON)", example = "[\"2024-04-05\", \"2024-04-10\"]")
  private String reservationAvailableDays;

  @Schema(description = "선약금", example = "10000")
  private BigDecimal depositAmount;

  @Schema(description = "가게 이미지 목록 (URL)")
  private List<String> imageUrls;

  @Schema(description = "메뉴 정보 목록")
  private List<RestaurantMenuRequest> menus;

  @Schema(description = "운영 시간 목록")
  private List<RestaurantOperatingHoursRequest> operatingHours;

  @Schema(description = "좌석 목록")
  private List<RestaurantSeatRequest> seats;
}
