package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantUpdateRequest {

  @Schema(description = "가게 ID", required = true)
  private Long restaurantId;

  @Schema(description = "가게명", example = "맛있는 돈가스")
  private String name;

  @Schema(description = "카테고리", example = "한식")
  private String category;

  @Schema(description = "연락처", example = "02-1234-5678")
  private String phoneNumber;

  @Schema(description = "주소", example = "서울시 강남구")
  private String address;

  @Schema(description = "자동 승인 여부", example = "true")
  private Boolean autoConfirm;

  @Schema(description = "관리자 승인 제한 시간 (분)", example = "60")
  private Integer approvalTimeout;

  @Schema(description = "예약 가능 날짜 설정 (JSON)")
  private String specialBookingDays;

  @Schema(description = "예약 신청 유형", example = "ANYTIME, WEEKLY, MONTHLY")
  private String reservationOpenType;

  @Schema(description = "예약 신청 가능 요일/날짜 (JSON)")
  private String reservationOpenDays;

  @Schema(description = "예약 가능 날짜 설정 (JSON)")
  private String reservationAvailableDays;

  @Schema(description = "선약금", example = "10000")
  private BigDecimal depositAmount;

  @Schema(description = "추가할 가게 이미지 목록 (URL)")
  private List<String> addImageUrls;

  @Schema(description = "삭제할 가게 이미지 ID 목록")
  private List<Long> deleteImageIds;
}
