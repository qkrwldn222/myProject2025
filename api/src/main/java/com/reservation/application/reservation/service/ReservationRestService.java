package com.reservation.application.reservation.service;

import com.reservation.application.payment.service.PaymentService;
import com.reservation.application.refund.service.RefundService;
import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.application.reservation.model.ReservationSearchCommand;
import com.reservation.application.restaurant.restaurant.service.RestaurantService;
import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.ReservationStatus;
import com.reservation.common.enums.RoleType;
import com.reservation.common.utils.DateTimeUtils;
import com.reservation.common.utils.JsonUtils;
import com.reservation.common.utils.SecurityUtil;
import com.reservation.domain.*;
import com.reservation.infrastructure.payment.model.TossPaymentResponse;
import com.reservation.infrastructure.reservation.repository.ReservationJpaRepository;
import com.reservation.infrastructure.reservation.specification.ReservationSpecification;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ReservationRestService implements ReservationService {

  private static final Logger logger = LoggerFactory.getLogger(ReservationRestService.class);
  private static final int EXPIRED_TIME = 10;

  private final RedisTemplate<String, String> redisTemplate;

  private final ReservationJpaRepository reservationRepository;
  private final RestaurantService restaurantService;
  private final UserService userService;
  private final RefundService refundService;

  private final PaymentService paymentService;

  @Override
  @Transactional
  public void cancelReservation(Long reservationId) {
    String currentUserId = SecurityUtil.getCurrentUserId();

    Reservation reservation =
        reservationRepository
            .findById(reservationId)
            .orElseThrow(() -> new ApiException("예약을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));

    if (!reservation.getUser().getUserID().equals(currentUserId) || !userService.isAdmin())
      throw new ApiException("예약 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);

    if (reservation.getStatus().equals(ReservationStatus.CANCELED)) {
      throw new ApiException("이미 취소된 예약입니다.", HttpStatus.BAD_REQUEST);
    }

    // 예약 시간이 지난 경우 취소 불가
    if (DateTimeUtils.toLocalDateTime(
            reservation.getReservationDate(), reservation.getReservationTime())
        .isBefore(LocalDateTime.now())) {
      throw new ApiException("이미 지난 예약은 취소할 수 없습니다.", HttpStatus.BAD_REQUEST);
    }

    // 예약금 환불 처리 (환불 정책 적용)
    if (reservation.getDepositAmount() != null
        && reservation.getDepositAmount().compareTo(BigDecimal.ZERO) > 0) {
      BigDecimal refundAmount = calculateRefundAmount(reservation);

      if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
        //                paymentService.refundPayment(reservation.getPaymentKey(),
        // reservation.getDepositAmount(), refundAmount);
      }
    }

    reservation.cancel();
    reservationRepository.save(reservation);

    // Redis 데이터 삭제 (동일 좌석 & 사용자 예약 삭제)
    String seatKey =
        "reservation:"
            + reservation.getRestaurant().getId()
            + ":"
            + reservation.getSeat().getSeatId()
            + ":"
            + reservation.getReservationDate()
            + ":"
            + reservation.getReservationTime();
    String userKey =
        "user-reservation:"
            + reservation.getUser().getId()
            + ":"
            + reservation.getReservationDate()
            + ":"
            + reservation.getReservationTime();

    redisTemplate.delete(seatKey);
    redisTemplate.delete(userKey);
  }

  @Override
  @Transactional
  public Reservation createReservation(ReservationRequestCommand command) {
    command.validate();
    String seatKey =
        "reservation:"
            + command.getRestaurantId()
            + ":"
            + command.getSeatId()
            + ":"
            + command.getReservationDate()
            + ":"
            + command.getReservationTime();
    String userKey =
        "user-reservation:"
            + command.getUserId()
            + ":"
            + command.getReservationDate()
            + ":"
            + command.getReservationTime();

    // Redis에서 동일 시간대 다른 가게 예약 확인
    Set<String> userReservationKeys =
        redisTemplate.keys(
            "user-reservation:" + command.getUserId() + ":" + command.getReservationDate() + ":*");

    if (!CollectionUtils.isEmpty(userReservationKeys)) {
      throw new ApiException("이미 동일 시간대에 예약된 가게가 있습니다.", HttpStatus.BAD_REQUEST);
    }

    // Redis에서  중복 예약 확인
    if (Boolean.TRUE.equals(redisTemplate.hasKey(seatKey))) {
      throw new ApiException("이미 해당 좌석이 예약되었습니다.", HttpStatus.BAD_REQUEST);
    }

    Reservation reservation = saveReservation(command);

    // Redis에 예약 정보 저장 (만료 시간: 10분)
    redisTemplate.opsForValue().set(seatKey, "reserved", EXPIRED_TIME, TimeUnit.MINUTES);
    redisTemplate.opsForValue().set(userKey, "reserved", EXPIRED_TIME, TimeUnit.MINUTES);

    return reservation;
  }

  @Transactional
  public void confirmReservation(Long reservationId) {
    String currentUserId = SecurityUtil.getCurrentUserId();

    Reservation reservation =
        reservationRepository
            .findById(reservationId)
            .orElseThrow(() -> new ApiException("예약을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

    User user =
        userService
            .findByUserId(currentUserId)
            .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST));

    Restaurant restaurant = reservation.getRestaurant();

    // 가게 운영자 또는 관리자만 예약 승인 가능하도록 검증
    if (!restaurant.getOwnerId().equals(user.getId())
        && !user.getRole().getRoleType().equals(RoleType.ADMIN)) {
      throw new ApiException("예약을 승인할 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    if (!reservation.getStatus().equals(ReservationStatus.PENDING)) {
      throw new ApiException("이미 처리된 예약입니다.", HttpStatus.BAD_REQUEST);
    }

    // 예약금이 있는 경우 → 결제 완료 확인 필요
    if (reservation.getDepositAmount() != null
        && reservation.getDepositAmount().compareTo(BigDecimal.ZERO) > 0) {
      if (reservation.getPaymentKey() == null) {
        throw new ApiException("예약금 결제가 완료되지 않았습니다.");
      }
    }

    // 승인 대기 시간이 초과 시 자동 취소
    LocalDateTime now = LocalDateTime.now();

    if (now.isAfter(reservation.getExpiresAt())) {
      reservation.cancel();
      reservationRepository.save(reservation);
      throw new ApiException("승인 대기 시간이 초과되어 예약이 자동 취소되었습니다.");
    }

    reservation.confirm();
    reservation.setExpiresAt(null); // 승인 제한 시간 제거
    reservationRepository.save(reservation);
  }

  private Reservation saveReservation(ReservationRequestCommand command) {
    Restaurant restaurant =
        restaurantService
            .findById(command.getRestaurantId())
            .orElseThrow(() -> new ApiException("레스토랑을 찾을 수 없습니다."));

    User user =
        userService
            .findById(command.getUserId())
            .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다."));

    RestaurantSeat seat =
        restaurantService
            .findBySeatIdAndRestaurantId(command.getSeatId(), command.getRestaurantId())
            .orElseThrow(() -> new ApiException("좌석을 찾을 수 없습니다."));

    // 예약 신청 가능한 날짜인지 검증
    if (!isRequestDateAllowed(restaurant, LocalDate.now())) {
      throw new ApiException("이 날짜에는 예약 신청이 불가능합니다.");
    }

    // 실제 예약이 가능한 날짜인지 검증
    if (!isReservationAllowed(restaurant, LocalDate.now(), command.getReservationDate())) {
      throw new ApiException("예약이 불가능한 날짜입니다.");
    }

    if (!seat.getIsAvailable()) {
      throw new ApiException("해당 좌석은 사용 불가능합니다.");
    }

    // 선약금이 있는 경우 결제 먼저 수행
    BigDecimal depositAmount = restaurant.getDepositAmount();

    TossPaymentResponse requestPayment = null;
    if (depositAmount != null && depositAmount.compareTo(BigDecimal.ZERO) > 0) {
      Optional<User> User = userService.findById(command.getUserId());
      //            requestPayment = (TossPaymentResponse)
      // paymentService.requestPayment(depositAmount, User.get().getUsername());
      if (!StringUtils.hasText(requestPayment.getPaymentKey())) {
        throw new ApiException("예약금 결제 실패");
      }
    }

    Reservation reservation =
        Reservation.builder()
            .restaurant(restaurant)
            .user(user)
            .seat(seat)
            .reservationDate(command.getReservationDate())
            .reservationTime(command.getReservationTime())
            .status(
                restaurant.getAutoConfirm() && depositAmount.equals(BigDecimal.ZERO)
                    ? ReservationStatus.CONFIRMED
                    : ReservationStatus.PENDING)
            .depositAmount(
                restaurant.getDepositAmount().equals(BigDecimal.ZERO)
                    ? BigDecimal.ZERO
                    : restaurant.getDepositAmount())
            .paymentKey(
                StringUtils.hasText(requestPayment.getPaymentKey())
                    ? requestPayment.getPaymentKey()
                    : UUID.randomUUID().toString())
            .orderId(
                StringUtils.hasText(requestPayment.getOrderId())
                    ? requestPayment.getOrderId()
                    : null)
            .expiresAt(
                restaurant.getAutoConfirm()
                    ? null
                    : LocalDateTime.now()
                        .plusMinutes(restaurant.getApprovalTimeout())) // 승인 제한 시간 설정
            .build();

    reservationRepository.save(reservation);

    return reservation;
  }

  @Scheduled(fixedRate = 60000) // 1분마다 실행
  @Transactional
  public void cancelExpiredReservations() {
    List<Reservation> expiredReservations =
        reservationRepository.findAllByStatusAndExpiresAtBefore(
            ReservationStatus.PENDING, LocalDateTime.now());

    for (Reservation reservation : expiredReservations) {
      reservation.cancel();
      reservationRepository.save(reservation);
      logger.info("자동 취소된 예약 ID: " + reservation.getReservationId());
    }
  }

  /**
   * 예약 신청 가능 여부 검증
   *
   * @param restaurant 음식점
   * @param requestDate 요청일
   * @return boolean
   */
  private boolean isRequestDateAllowed(Restaurant restaurant, LocalDate requestDate) {
    ReservationOpenType type = restaurant.getReservationOpenType();
    if (type == ReservationOpenType.ANYTIME) {
      return true;
    }

    List<String> openDays = JsonUtils.fromJsonToList(restaurant.getReservationOpenDays());

    if (type == ReservationOpenType.WEEKLY) {
      return openDays.contains(requestDate.getDayOfWeek().name());
    } else if (type == ReservationOpenType.MONTHLY) {
      return openDays.contains(String.valueOf(requestDate.getDayOfMonth()));
    }
    return false;
  }

  /**
   * 실제 예약 가능 여부 검증
   *
   * @param restaurant 가게
   * @param requestDate 예약 신청 일
   * @param reservationDate 예약일
   * @return boolean
   */
  private boolean isReservationAllowed(
      Restaurant restaurant, LocalDate requestDate, LocalDate reservationDate) {
    ReservationOpenType type = restaurant.getReservationOpenType();

    // 예약 가능일 범위 검증 (reservationDate)
    Map<String, String> dateConfig =
        JsonUtils.parseJsonToMap(restaurant.getReservationAvailableDays());
    int minDays = Integer.parseInt(dateConfig.getOrDefault("minDays", "1"));
    int maxDays = Integer.parseInt(dateConfig.getOrDefault("maxDays", "365"));

    LocalDate minReservationDate = requestDate.plusDays(minDays);
    LocalDate maxReservationDate = requestDate.plusDays(maxDays);

    return !(reservationDate.isBefore(minReservationDate)
        || reservationDate.isAfter(maxReservationDate));
  }

  /**
   * 환불금액 계산
   *
   * @param reservation 예약 도메인
   * @return 환불금액
   */
  private BigDecimal calculateRefundAmount(Reservation reservation) {
    if (reservation.getDepositAmount() == null
        || reservation.getDepositAmount().compareTo(BigDecimal.ZERO) == 0) {
      return BigDecimal.ZERO; // 예약금이 없는 경우 환불 없음
    }

    List<RefundPolicy> policies =
        refundService.findByRestaurantId(reservation.getRestaurant().getId());
    long daysBefore = ChronoUnit.DAYS.between(LocalDate.now(), reservation.getReservationDate());

    // 예약 가능일 기준 가장 유리한 환불 정책 조회
    Optional<RefundPolicy> applicablePolicy =
        policies.stream()
            .filter(policy -> daysBefore >= policy.getDaysBefore())
            .max(Comparator.comparingInt(RefundPolicy::getDaysBefore));

    if (applicablePolicy.isPresent()) {
      BigDecimal refundPercentage =
          applicablePolicy.get().getRefundPercentage().divide(BigDecimal.valueOf(100));
      return reservation.getDepositAmount().multiply(refundPercentage);
    }

    return BigDecimal.ZERO; // 환불 정책이 없으면 환불 없음
  }

  @Override
  @Transactional(readOnly = true)
  public List<Reservation> findReservations(ReservationSearchCommand command) {
    String currentUserId = SecurityUtil.getCurrentUserId();

    // 현재 사용자 조회 (권한 체크용)
    User currentUser =
        userService
            .findByUserId(currentUserId)
            .orElseThrow(() -> new ApiException("로그인 정보를 찾을 수 없습니다."));

    RoleType currentUserRole = currentUser.getRole().getRoleType();
    Long ownerRestaurantId = null;

    // OWNER일 경우 소유한 Restaurant 조회
    if (RoleType.STORE_OWNER.equals(currentUserRole)) {
      Restaurant restaurant =
          restaurantService
              .findByOwnerId(currentUser.getId())
              .orElseThrow(() -> new ApiException("운영하는 가게가 없습니다."));
      ownerRestaurantId = restaurant.getId();
    }

    Specification<Reservation> spec =
        ReservationSpecification.search(
            command.getUserId(),
            command.getRestaurant_id(),
            command.getReservationStatus(),
            command.getReservationStarDate(),
            command.getReservationEndDate(),
            currentUser.getUserID(),
            currentUserRole,
            ownerRestaurantId);

    return reservationRepository.findAll(spec);
  }
}
