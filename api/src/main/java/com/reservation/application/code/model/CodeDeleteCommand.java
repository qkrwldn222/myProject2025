package com.reservation.application.code.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Data
@NoArgsConstructor
public class CodeDeleteCommand implements BaseValidation {

  private Long id;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(id)) {
      throw new ApiException("ID는 필수 값 입니다.");
    }
  }
}
