package com.reservation.application.reservation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.reservation.application.payment.service.PaymentService;
import com.reservation.application.refund.service.RefundService;
import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.application.reservation.service.ReservationRestService;
import com.reservation.application.restaurant.restaurant.service.RestaurantService;
import com.reservation.application.usecase.ReservationEventUseCase;
import com.reservation.application.user.service.UserService;
import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.ReservationOpenType;
import com.reservation.common.enums.ReservationStatus;
import com.reservation.common.enums.RestaurantStatus;
import com.reservation.domain.Reservation;
import com.reservation.domain.Restaurant;
import com.reservation.domain.RestaurantOperatingHours;
import com.reservation.domain.RestaurantSeat;
import com.reservation.domain.User;
import com.reservation.infrastructure.reservation.repository.ReservationJpaRepositoryAdapter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
class ReservationRestServiceUnitTest {

  @Mock private RedisTemplate<String, String> redisTemplate;
  @Mock private ValueOperations<String, String> valueOperations;
  @Mock private ReservationJpaRepositoryAdapter reservationRepository;
  @Mock private RestaurantService restaurantService;
  @Mock private UserService userService;
  @Mock private RefundService refundService;
  @Mock private ReservationEventUseCase reservationEventUseCase;
  @Mock private PaymentService<Object> paymentService;

  private ReservationRestService reservationRestService;

  @BeforeEach
  void setUp() {
    reservationRestService =
        new ReservationRestService(
            redisTemplate,
            reservationRepository,
            restaurantService,
            userService,
            refundService,
            reservationEventUseCase,
            paymentService);
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  void createReservationWorksWhenDepositAmountIsNull() {
    Long restaurantId = 1L;
    Long seatId = 2L;
    Long userId = 3L;
    LocalDate reservationDate = LocalDate.now().plusDays(2);
    LocalTime reservationTime = LocalTime.of(12, 0);

    ReservationRequestCommand command = new ReservationRequestCommand();
    command.setRestaurantId(restaurantId);
    command.setSeatId(seatId);
    command.setUserId(userId);
    command.setReservationDate(reservationDate);
    command.setReservationTime(reservationTime);

    Restaurant restaurant =
        Restaurant.builder()
            .id(restaurantId)
            .name("test")
            .category("korean")
            .phoneNumber("000")
            .address("seoul")
            .reservationOpenType(ReservationOpenType.ANYTIME)
            .reservationAvailableDays("{\"minDays\":1,\"maxDays\":30}")
            .status(RegistrationStatus.APPROVED)
            .managementStatus(RestaurantStatus.OPEN)
            .autoConfirm(true)
            .approvalTimeout(10)
            .depositAmount(null)
            .build();

    RestaurantSeat seat =
        RestaurantSeat.builder()
            .seatId(seatId)
            .restaurant(restaurant)
            .seatNumber("A1")
            .maxCapacity(2)
            .isAvailable(true)
            .build();

    RestaurantOperatingHours operatingHours =
        RestaurantOperatingHours.builder()
            .restaurant(restaurant)
            .openTime(LocalTime.of(9, 0))
            .closeTime(LocalTime.of(21, 0))
            .isHoliday(false)
            .reservationInterval(60)
            .build();

    User user = User.builder().id(userId).userID("user1").username("user1").build();

    when(valueOperations.setIfAbsent(anyString(), eq("reserved"), anyLong(), any()))
        .thenReturn(true);
    when(redisTemplate.keys(anyString())).thenReturn(Collections.emptySet());
    when(restaurantService.findById(restaurantId)).thenReturn(Optional.of(restaurant));
    when(restaurantService.findByRestaurantIdAndDayOfWeek(eq(restaurantId), any()))
        .thenReturn(Optional.of(operatingHours));
    when(userService.findById(userId)).thenReturn(Optional.of(user));
    when(restaurantService.findBySeatIdAndRestaurantId(seatId, restaurantId))
        .thenReturn(Optional.of(seat));
    when(reservationRepository.existsBySeatAndReservationDateAndReservationTime(
            seat, reservationDate, reservationTime))
        .thenReturn(false);
    when(reservationRepository.save(any(Reservation.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Reservation reservation = reservationRestService.createReservation(command);

    assertNotNull(reservation);
    assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
    assertNotNull(reservation.getDepositAmount());
    assertEquals(0, reservation.getDepositAmount().compareTo(java.math.BigDecimal.ZERO));
    verify(paymentService, never()).requestPayment(any(), anyString());
  }
}
