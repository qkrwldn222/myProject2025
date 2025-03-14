package com.reservation.common.valid;

public interface BaseValidation<T> {
  void validate(); // 모든 구현체에서 필수 값 검증을 구현하도록 강제
}
