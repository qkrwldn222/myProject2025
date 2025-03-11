package com.reservation.application.reservation.service;

import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.common.config.ApiException;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.ReservationStatus;
import com.reservation.common.enums.RoleType;
import com.reservation.common.utils.DateTimeUtils;
import com.reservation.common.utils.JsonUtils;
import com.reservation.domain.Reservation;
import com.reservation.domain.Restaurant;
import com.reservation.domain.RestaurantSeat;
import com.reservation.domain.User;
import com.reservation.infrastructure.reservation.repository.ReservationJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantJpaRepository;
import com.reservation.infrastructure.restaurant.seat.RestaurantSeatJpaRepository;
import com.reservation.infrastructure.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationRestService implements ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationRestService.class);
    private static final int EXPIRED_TIME = 10;

    private final RedisTemplate<String, String> redisTemplate;

    private final ReservationJpaRepository reservationRepository;
    private final RestaurantJpaRepository restaurantRepository;
    private final UserJpaRepository userRepository;
    private final RestaurantSeatJpaRepository seatRepository;




    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException("예약을 찾을 수 없습니다."));

        if (reservation.getStatus().equals(ReservationStatus.CANCELED)) {
            throw new ApiException("이미 취소된 예약입니다.");
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);

        // Redis 데이터 삭제 (동일 좌석 & 사용자 예약 삭제)
        String seatKey = "reservation:" + reservation.getRestaurant().getId() + ":" + reservation.getSeat().getSeatId() + ":" + reservation.getReservationDate() + ":" + reservation.getReservationTime();
        String userKey = "user-reservation:" + reservation.getUser().getId() + ":" + reservation.getReservationDate() + ":" + reservation.getReservationTime();

        redisTemplate.delete(seatKey);
        redisTemplate.delete(userKey);
    }

    @Override
    @Transactional
    public Reservation createReservation(ReservationRequestCommand command) {
        command.validate();
        String seatKey = "reservation:" + command.getRestaurantId() + ":" + command.getSeatId() + ":" + command.getReservationDate() + ":" + command.getReservationTime();
        String userKey = "user-reservation:" + command.getUserId() + ":" + command.getReservationDate() + ":" + command.getReservationTime();


        // Redis에서 동일 시간대 다른 가게 예약 확인
        Set<String> userReservationKeys = redisTemplate.keys("user-reservation:" + command.getUserId() + ":" + command.getReservationDate() + ":*");

        if (!CollectionUtils.isEmpty(userReservationKeys)) {
            throw new ApiException("이미 동일 시간대에 예약된 가게가 있습니다.");
        }

        // Redis에서  중복 예약 확인
        if (Boolean.TRUE.equals(redisTemplate.hasKey(seatKey))) {
            throw new ApiException("이미 해당 좌석이 예약되었습니다.");
        }

        Reservation reservation = saveReservation(command);

        // Redis에 예약 정보 저장 (만료 시간: 10분)
        redisTemplate.opsForValue().set(seatKey, "reserved", EXPIRED_TIME, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(userKey, "reserved", EXPIRED_TIME, TimeUnit.MINUTES);

        return reservation;
    }

    @Transactional
    public void confirmReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException("예약을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다."));

        Restaurant restaurant = reservation.getRestaurant();

        // 가게 운영자 또는 관리자만 예약 승인 가능하도록 검증
        if (!restaurant.getOwnerUser().getId().equals(user.getId()) && !user.getRole().getRoleType().equals(RoleType.ADMIN)) {
            throw new ApiException("예약을 승인할 권한이 없습니다.");
        }

        if (!reservation.getStatus().equals(ReservationStatus.PENDING)) {
            throw new ApiException("이미 처리된 예약입니다.");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setExpiresAt(null); // 승인 제한 시간 제거
        reservationRepository.save(reservation);
    }

    private Reservation saveReservation(ReservationRequestCommand command) {

        Restaurant restaurant = restaurantRepository.findById(command.getRestaurantId())
                .orElseThrow(() -> new ApiException("레스토랑을 찾을 수 없습니다."));

        User user = userRepository.findById(command.getUserId())
                .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다."));

        RestaurantSeat seat = seatRepository.findById(command.getSeatId())
                .orElseThrow(() -> new ApiException("좌석을 찾을 수 없습니다."));

        // 예약 신청 가능한 날짜인지 검증
        if (!isReservationRequestAllowed(restaurant, LocalDate.now())) {
            throw new ApiException("이 날짜에는 예약 신청이 불가능합니다.");
        }

        // 실제 예약이 가능한 날짜인지 검증
        if (!isReservationAllowed(restaurant, LocalDate.now())) {
            throw new ApiException("예약이 불가능한 날짜입니다.");
        }

        if (!seat.getIsAvailable()) {
            throw new ApiException("해당 좌석은 사용 불가능합니다.");
        }

        Reservation reservation = Reservation.builder()
                .restaurant(restaurant)
                .user(user)
                .seat(seat)
                .reservationDate(command.getReservationDate())
                .reservationTime(command.getReservationTime())
                .status(restaurant.getAutoConfirm() ? ReservationStatus.CONFIRMED : ReservationStatus.PENDING)
                .expiresAt(restaurant.getAutoConfirm() ? null :
                        LocalDateTime.now().plusMinutes(restaurant.getApprovalTimeout())) // 승인 제한 시간 설정
                .build();

        reservationRepository.save(reservation);
        return reservation;
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    @Transactional
    public void cancelExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository.findAllByStatusAndExpiresAtBefore(
                ReservationStatus.PENDING, LocalDateTime.now());

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.CANCELED);
            reservationRepository.save(reservation);
            logger.info("자동 취소된 예약 ID: " + reservation.getReservationId());
        }
    }


    /**
     * 예약 신청 가능 여부 검증
     * @param restaurant 음식점
     * @param requestDate 요청일
     * @return boolean
     */
    private boolean isReservationRequestAllowed(Restaurant restaurant, LocalDate requestDate) {
        if (!StringUtils.hasText(restaurant.getReservationOpenDays())) return true;

        List<String> openDays = JsonUtils.fromJsonToList(restaurant.getReservationOpenDays());

        if (restaurant.getReservationOpenType() == ReservationOpenType.ANYTIME) {
            return true;
        } else if (restaurant.getReservationOpenType() == ReservationOpenType.WEEKLY) {
            return openDays.contains(requestDate.getDayOfWeek().name());
        } else if (restaurant.getReservationOpenType() == ReservationOpenType.MONTHLY) {
            return openDays.contains(String.valueOf(requestDate.getDayOfMonth()));
        }
        return false;
    }

    /**
     * 실제 예약 가능 여부 검증
     * @param restaurant 음식점
     * @param reservationDate 예약일
     * @return boolean
     */
    private boolean isReservationAllowed(Restaurant restaurant, LocalDate reservationDate) {
        if (!StringUtils.hasText(restaurant.getReservationAvailableDays())) return true;

        // JSON → Map 변환 후, 날짜 비교 수행
        Map<String, String> dateConfig = JsonUtils.parseJsonToMap(restaurant.getReservationAvailableDays());
        return DateTimeUtils.isDateWithinAvailableRange(dateConfig, reservationDate);
    }
}
