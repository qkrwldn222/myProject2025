package com.reservation.infrastructure.payment.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.infrastructure.payment.model.TossPaymentConfirmRequest;
import com.reservation.infrastructure.payment.model.TossPaymentErrorResponse;
import com.reservation.infrastructure.payment.model.TossPaymentRequest;
import com.reservation.infrastructure.payment.model.TossPaymentResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

// @Repository
@RequiredArgsConstructor
public class TossPaymentExternalRepository {
  private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments";
  private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
  private static final String TOSS_REFUND_URL = "https://api.tosspayments.com/v1/payments/refund";

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${toss.secret-key}")
  private String secretKey;

  @Value("${toss.success-url}")
  private String successUrl;

  @Value("${toss.fail-url}")
  private String failUrl;

  /**
   * 결제 요청
   *
   * @param amount 금액
   * @param customerName 예약자명
   * @return PaymentKey
   */
  public TossPaymentResponse requestPayment(BigDecimal amount, String customerName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBasicAuth(secretKey, ""); // 기본 인증 (Base64 인코딩됨)

    TossPaymentRequest request =
        TossPaymentRequest.builder()
            .amount(amount)
            .orderId("RES-" + System.currentTimeMillis())
            .orderName("식당 예약금 결제")
            .successUrl(successUrl)
            .failUrl(failUrl)
            .customerName(customerName)
            .build();

    HttpEntity<TossPaymentRequest> requestEntity = new HttpEntity<>(request, headers);

    try {
      ResponseEntity<TossPaymentResponse> response =
          restTemplate.exchange(
              TOSS_API_URL, HttpMethod.POST, requestEntity, TossPaymentResponse.class);
      return response.getBody();
    } catch (HttpStatusCodeException e) {
      handlePaymentError(e);
      return null;
    }
  }

  /**
   * 결제 승인
   *
   * @param paymentKey 결제 키
   * @param orderId 예약 키
   * @param amount 금액
   * @return TossPaymentResponse
   */
  public TossPaymentResponse confirmPayment(String paymentKey, String orderId, BigDecimal amount) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBasicAuth(secretKey, "");

    TossPaymentConfirmRequest request =
        TossPaymentConfirmRequest.builder()
            .paymentKey(paymentKey)
            .orderId(orderId)
            .amount(amount)
            .build();

    HttpEntity<TossPaymentConfirmRequest> requestEntity = new HttpEntity<>(request, headers);

    try {
      ResponseEntity<TossPaymentResponse> response =
          restTemplate.exchange(
              TOSS_CONFIRM_URL, HttpMethod.POST, requestEntity, TossPaymentResponse.class);
      return response.getBody();
    } catch (HttpStatusCodeException e) {
      handlePaymentError(e);
      return null;
    }
  }

  public TossPaymentResponse refundPayment(
      String paymentKey, BigDecimal originalAmount, BigDecimal refundAmount) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBasicAuth(secretKey, "");

    Map<String, Object> body = new HashMap<>();
    body.put("paymentKey", paymentKey);
    body.put("cancelReason", "고객 예약 취소");
    body.put("cancelAmount", refundAmount);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    try {
      ResponseEntity<TossPaymentResponse> response =
          restTemplate.exchange(
              TOSS_REFUND_URL, HttpMethod.POST, requestEntity, TossPaymentResponse.class);
      return response.getBody();
    } catch (HttpStatusCodeException e) {
      handlePaymentError(e);
      return null;
    }
  }

  /**
   * 오류 처리
   *
   * @param e exception
   */
  private void handlePaymentError(HttpStatusCodeException e) {
    try {
      TossPaymentErrorResponse errorResponse =
          objectMapper.readValue(e.getResponseBodyAsString(), TossPaymentErrorResponse.class);
      throw new RuntimeException(
          "결제 오류: " + errorResponse.getMessage() + " (코드: " + errorResponse.getCode() + ")");
    } catch (Exception ex) {
      throw new RuntimeException("결제 오류: " + e.getMessage());
    }
  }
}
