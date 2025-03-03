package com.reservation.application.store.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FoodStoreSearchCommand {
  private Long mgtNo; // 관리번호
  private String bplcNm; // 사업장명
  private String rdnWhlAddr; // 도로명 주소
}
