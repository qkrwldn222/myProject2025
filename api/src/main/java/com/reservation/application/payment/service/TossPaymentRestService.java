package com.reservation.application.payment.service;

import com.reservation.infrastructure.payment.model.TossPaymentResponse;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TossPaymentRestService implements PaymentService<TossPaymentResponse> {
  //      private final TossPaymentExternalRepository tossPaymentExternalRepository;

  @Override
  public TossPaymentResponse requestPayment(BigDecimal amount, String customerName) {
    TossPaymentResponse response =
        new TossPaymentResponse(
            customerName + amount.toString(), UUID.randomUUID().toString(), "DONE", amount);

    return response;
    //        return tossPaymentExternalRepository.requestPayment( amount, customerName);
  }

  @Override
  public void confirmPayment(String paymentKey, String orderId, BigDecimal amount) {
    //        tossPaymentExternalRepository.confirmPayment(paymentKey, orderId, amount);
  }

  @Override
  public void refundPayment(String paymentKey, BigDecimal originalAmount, BigDecimal refundAmount) {
    //        tossPaymentExternalRepository.refundPayment(paymentKey, originalAmount, refundAmount);
  }
}
