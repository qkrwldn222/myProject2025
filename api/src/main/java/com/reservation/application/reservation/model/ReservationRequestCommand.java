package com.reservation.application.reservation.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReservationRequestCommand implements BaseValidation {

  private Long restaurantId;
  private Long userId;
  private Long seatId;
  private LocalDate reservationDate;
  private LocalTime reservationTime;

  @Override
  public void validate() {
    if (Objects.isNull(restaurantId) || Objects.isNull(userId) || Objects.isNull(seatId)) {
      throw new ApiException("예약 정보를 입력해주세요.");
    }
    if (Objects.isNull(reservationDate) || Objects.isNull(reservationTime)) {
      throw new ApiException("예약 날짜와 시간을 입력해주세요.");
    }
  }
}
