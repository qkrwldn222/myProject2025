package com.reservation.infrastructure.payment.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TossPaymentConfirmRequest {
  private String paymentKey; // 결제 승인 키
  private String orderId; // 주문 ID
  private BigDecimal amount; // 결제 금액
}
