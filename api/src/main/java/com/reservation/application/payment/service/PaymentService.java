package com.reservation.application.payment.service;

import java.math.BigDecimal;

public interface PaymentService<T> {
    T requestPayment(BigDecimal amount, String customerName);

    void refundPayment(String paymentKey, BigDecimal originalAmount, BigDecimal refundAmount);

    void confirmPayment(String paymentKey, String orderId, BigDecimal amount);
}
