package com.reservation.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiResponseWrapper<T> {
  private final String message;
  private final String state;
  private final T result;
  private final Long createdId;

  /** 단건 조회 성공 */
  public static <T> ApiResponseWrapper<T> singleResult(String message, T data) {
    return new ApiResponseWrapper<>(message, "SUCCESS", data, null);
  }

  /** 기능 수행 응답 (CUD) */
  public static ApiResponseWrapper<Void> actionResult(String message) {
    return new ApiResponseWrapper<>(message, "SUCCESS", null, null);
  }

  /** 생성 응답 (ID 포함 가능) */
  public static ApiResponseWrapper<Long> createResult(String message, Long id) {
    return new ApiResponseWrapper<>(message, "SUCCESS", null, id);
  }
}
