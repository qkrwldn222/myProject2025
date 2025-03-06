package com.reservation.application.code.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class CodeSaveCommand implements BaseValidation {
  private String code;
  private String name;
  private String groupCode;
  private String attr01;
  private String attr02;
  private String attr03;
  private String attr04;
  private String attr05;
  private String attr06;
  private String attr07;
  private String attr08;
  private String attr09;

  @Override
  public void validate() {
    if (!StringUtils.hasText(code)) {
      throw new ApiException(("code는 필수 값 입니다."));
    }

    if (!StringUtils.hasText(groupCode)) {
      throw new ApiException(("group code는 필수 값 입니다."));
    }
  }
}
