package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.enums.SeatType;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@NoArgsConstructor
public class RestaurantSeatCreateCommand implements BaseValidation {

  private SeatType seatType;
  private String seatNumber;
  private int maxCapacity;
  private boolean isAvailable;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(seatType)
        || ObjectUtils.isEmpty(seatNumber)
        || ObjectUtils.isEmpty(maxCapacity)) {
      throw new ApiException("좌석 타잎, 번호, 수용인원을 값은 필수 입니다.");
    }
  }
}
