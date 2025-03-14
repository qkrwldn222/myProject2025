package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@NoArgsConstructor
public class RestaurantApprovalCommand implements BaseValidation {

  Long restaurantId;
  RegistrationStatus state;
  String rejectedReason;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(state)
        || (!state.equals(RegistrationStatus.APPROVED)
            && !state.equals(RegistrationStatus.REJECTED))) {
      throw new ApiException("상태 값을 확인해 주세요.");
    }
  }
}
