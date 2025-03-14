package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.utils.JsonUtils;
import com.reservation.common.valid.BaseValidation;
import com.reservation.common.valid.RestaurantValidator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class RestaurantCreateCommand implements BaseValidation {
  private String name;
  private String category;
  private String phoneNumber;
  private String address;
  private Long ownerId;
  private Boolean autoConfirm;
  private Integer approvalTimeout;
  private String specialBookingDays;
  private ReservationOpenType reservationOpenType;
  private String reservationOpenDays;
  private String reservationAvailableDays;
  private BigDecimal depositAmount;
  private List<String> imageUrls;
  private List<RestaurantMenuCommand> menus;
  private List<RestaurantOperatingHoursCommand> operatingHours;
  private List<RestaurantSeatCommand> seats;

  @Override
  public void validate() {
    if (!StringUtils.hasText(name)
        || !StringUtils.hasText(category)
        || !StringUtils.hasText(address)) {
      throw new ApiException("가게명, 카테고리, 주소는 필수값입니다.");
    }

    if (reservationOpenType == null
        || !StringUtils.hasText(reservationOpenDays)
        || !StringUtils.hasText(reservationAvailableDays)) {
      throw new ApiException(
          "예약 설정(reservationOpenType, reservationOpenDays, reservationAvailableDays)을 모두 입력해야 합니다.");
    }

    List<String> openDaysList = JsonUtils.fromJsonToList(reservationOpenDays);
    Map<String, String> availableDaysMap = JsonUtils.parseJsonToMap(reservationAvailableDays);

    new RestaurantValidator()
        .validateReservationDays(openDaysList, availableDaysMap, reservationOpenType);
  }
}
