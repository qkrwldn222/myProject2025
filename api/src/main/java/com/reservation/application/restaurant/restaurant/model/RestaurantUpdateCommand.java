package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.RestaurantStatus;
import com.reservation.common.valid.BaseValidation;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class RestaurantUpdateCommand implements BaseValidation {
  private Long restaurantId;
  private String name;
  private String category;
  private String phoneNumber;
  private String address;
  private Boolean autoConfirm;
  private Integer approvalTimeout;
  private String specialBookingDays;
  private ReservationOpenType reservationOpenType;
  private RestaurantStatus restaurantStatus;
  private String reservationOpenDays;
  private String reservationAvailableDays;
  private BigDecimal depositAmount;
  private List<String> addImageUrls;
  private List<Long> deleteImageIds;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(restaurantId)) {
      throw new ApiException("가게 키가 없습니다.");
    }
    if (!StringUtils.hasText(name)) {
      throw new ApiException("가게명은 필수 값 입니다.");
    }
    if (!StringUtils.hasText(address)) {
      throw new ApiException("주소는 필수 값 입니다.");
    }
  }
}
