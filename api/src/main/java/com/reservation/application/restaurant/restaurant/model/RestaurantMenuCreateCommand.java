package com.reservation.application.restaurant.restaurant.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class RestaurantMenuCreateCommand implements BaseValidation {
  private String name;
  private String description;
  private BigDecimal price;
  private String imageUrl;
  private Boolean isBest;

  @Override
  public void validate() {
    if (!StringUtils.hasText(name)) {
      throw new ApiException("이름은 필수 값 입니다.");
    }
  }
}
