package com.reservation.application.code.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CodeUpdateCommand extends CodeSaveCommand implements BaseValidation {
  private Long id;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(id)) {
      throw new ApiException("id는 필수 값 입니다.");
    }
  }
}
