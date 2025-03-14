package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RestaurantMenuUpdateCommand extends RestaurantMenuCreateCommand
    implements BaseValidation {
  private Long id;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(id)) {
      throw new ApiException("id는 필수 값 입니다.");
    }
  }
}
