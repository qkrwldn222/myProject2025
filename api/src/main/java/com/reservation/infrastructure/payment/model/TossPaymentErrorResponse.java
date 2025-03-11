package com.reservation.infrastructure.payment.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TossPaymentErrorResponse {
    private String code;    // 오류 코드
    private String message; // 오류 메시지

    @JsonProperty("error")
    private String error;   // 상세 오류 정보
}
