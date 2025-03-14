package com.reservation.adapter.restaurant.model;

import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.ReservationOpenType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantSearchRegistrationsResponse {

  @Schema(description = "레스토랑 ID", example = "1")
  private Long restaurantId;

  @Schema(description = "레스토랑 이름", example = "해운대 씨푸드")
  private String name;

  @Schema(description = "카테고리", example = "양식")
  private String category;

  @Schema(description = "전화번호", example = "051-777-8888")
  private String phoneNumber;

  @Schema(description = "주소", example = "부산시 해운대구 해운대로 100")
  private String address;

  @Schema(description = "레스토랑 상태", example = "OPEN")
  private RegistrationStatus status;

  @Schema(description = "예약 보증금 (단위: 원)", example = "30000")
  private BigDecimal depositAmount;

  @Schema(description = "예약 가능일", example = "30000")
  private String reservationAvailableDays;

  @Schema(description = "예약가능 타입", example = "ANYTIME")
  private ReservationOpenType reservationOpenType;

  @Schema(description = "예약 오픈일", example = "['2024-05-05']")
  private String reservationOpenDays;

  @Schema(description = "예약 신청 가능 요일/날짜", example = "['2024-05-05']")
  private String specialBookingDays;

  @Schema(description = "메뉴 리스트")
  private List<RestaurantMenuResponse> menus;

  @Schema(description = "운영 시간 리스트")
  private List<RestaurantOperatingHoursResponse> operatingHours;

  @Schema(description = "좌석 리스트")
  private List<RestaurantSeatResponse> seats;
}
