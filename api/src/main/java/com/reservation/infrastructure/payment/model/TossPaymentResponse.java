package com.reservation.infrastructure.payment.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TossPaymentResponse {
  private String paymentKey; // 결제 승인 키
  private String orderId; // 주문 ID
  private String status; // 결제 상태 (DONE, CANCELLED 등)
  private BigDecimal amount; // 결제 금액
}
