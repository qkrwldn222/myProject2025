package com.reservation.infrastructure.payment.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossPaymentRequest {
  private BigDecimal amount; // 결제 금액
  private String orderId; // 주문 ID (예: RES-12345)
  private String orderName; // 결제 항목명 (예: "식당 예약금 결제")
  private String successUrl; // 결제 성공 시 리디렉트 URL
  private String failUrl; // 결제 실패 시 리디렉트 URL
  private String customerName; // 고객 이름
}
