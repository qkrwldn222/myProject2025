package com.reservation.application.payment.service;

import com.reservation.infrastructure.payment.model.TossPaymentResponse;
import com.reservation.infrastructure.payment.repository.TossPaymentExternalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TossPaymentRestService implements PaymentService<TossPaymentResponse> {
    private final TossPaymentExternalRepository tossPaymentExternalRepository;

    @Override
    public TossPaymentResponse requestPayment(BigDecimal amount, String customerName) {
        return tossPaymentExternalRepository.requestPayment( amount, customerName);
    }

    @Override
    public void confirmPayment(String paymentKey, String orderId, BigDecimal amount) {
        tossPaymentExternalRepository.confirmPayment(paymentKey, orderId, amount);
    }

    @Override
    public void refundPayment(String paymentKey, BigDecimal originalAmount, BigDecimal refundAmount) {
        tossPaymentExternalRepository.refundPayment(paymentKey, originalAmount, refundAmount);
    }
}
