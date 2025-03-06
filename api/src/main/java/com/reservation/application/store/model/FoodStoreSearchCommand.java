package com.reservation.application.store.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class FoodStoreSearchCommand implements BaseValidation {
  private Long mgtNo; // 관리번호
  private String bplcNm; // 사업장명
  private String rdnWhlAddr; // 도로명 주소
  private String trdStateGbn; // 영업 상태코드

  @Override
  public void validate() {
    if (!StringUtils.hasText(trdStateGbn)) {
      throw new ApiException("영업 상태코드는 필수 값 입니다.");
    }
  }
}
