package com.reservation.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "API 오류 응답")
public class ApiErrorResponse {

  @Schema(description = "에러 메시지")
  private final String message;

  @Schema(description = "HTTP 상태 코드")
  private final int statusCode;
}
