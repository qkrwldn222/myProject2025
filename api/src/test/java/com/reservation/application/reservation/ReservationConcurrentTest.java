package com.reservation.application.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.reservation.application.refund.service.RefundService;
import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.application.reservation.service.ReservationRestService;
import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.common.enums.*;
import com.reservation.domain.*;
import com.reservation.infrastructure.reservation.repository.ReservationJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantMenuJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantOperatingHoursJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantSeatJpaRepository;
import com.reservation.infrastructure.role.repository.RoleJpaRepository;
import com.reservation.infrastructure.user.repository.UserJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Rollback
// @Transactional
// @ActiveProfiles("test")
public class ReservationConcurrentTest {

  @Autowired private ReservationRestService reservationService;
  @Autowired private UserService userService;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private RefundService refundService;

  @Autowired private UserJpaRepository userRepository;

  @Autowired private RestaurantJpaRepository restaurantRepository;

  @Autowired private RestaurantSeatJpaRepository seatRepository;

  @Autowired private RestaurantMenuJpaRepository menuRepository;

  @Autowired private RestaurantOperatingHoursJpaRepository hoursRepository;

  @Autowired private RoleJpaRepository roleJpaRepository;

  private final ExecutorService executor = Executors.newFixedThreadPool(10);

  private final LocalDate reservationDate = LocalDate.now().plusDays(2);
  private final LocalTime reservationTime = LocalTime.of(12, 0);

  User[] testUsers = new User[10];
  Restaurant testRestaurant;
  RestaurantSeat testSeat;
    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

  @BeforeEach
  @Transactional
  void setUp() {
    // Testcontainers 사용하면 별도 작업 필요없음
    List<Role> all = roleJpaRepository.findAll();
    // 일반 User 10명 생성
    for (int i = 0; i < 10; i++) {
      testUsers[i] =
          userRepository.save(
              User.builder()
                  .userID("user" + i)
                  .username("user" + i)
                  .password("password")
                  .role(
                      all.stream()
                          .filter(role -> role.getRoleType().equals(RoleType.USER))
                          .findFirst()
                          .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다: ")))
                  .build());
    }

    // 관리자 유저 생성
    User admin =
        userRepository.save(
            User.builder()
                .userID("admin_user")
                .username("admin_user")
                .password("password")
                .role(
                    all.stream()
                        .filter(role -> role.getRoleType().equals(RoleType.ADMIN))
                        .findFirst()
                        .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다: ")))
                .build());

    // 가게 운영자 유저 생성
    User owner =
        userRepository.save(
            User.builder()
                .userID("owner_user")
                .username("owner_user")
                .password("password")
                .role(
                    all.stream()
                        .filter(role -> role.getRoleType().equals(RoleType.STORE_OWNER))
                        .findFirst()
                        .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다: ")))
                .build());

    // 레스토랑 생성
    testRestaurant =
        restaurantRepository.save(
            Restaurant.builder()
                .name("테스트 레스토랑")
                .category("한식")
                .phoneNumber("02-1234-5678")
                .address("서울시 강남구 테헤란로 123")
                .reservationOpenType(ReservationOpenType.ANYTIME)
                .reservationAvailableDays("{\"minDays\":1,\"maxDays\":30}")
                .status(RegistrationStatus.APPROVED)
                .managementStatus(RestaurantStatus.OPEN)
                .ownerId(owner.getId())
                .autoConfirm(true)
                .approvalTimeout(60)
                .reservationOpenType(ReservationOpenType.ANYTIME)
                .depositAmount(new BigDecimal("10000.00"))
                .build());

    // 레스토랑 좌석 생성
    testSeat =
        seatRepository.save(
            RestaurantSeat.builder()
                .restaurant(testRestaurant)
                .seatType(SeatType.TABLE)
                .seatNumber("A1")
                .maxCapacity(4)
                .isAvailable(true)
                .build());

    // 레스토랑 메뉴 생성
    menuRepository.save(
        RestaurantMenu.builder()
            .restaurant(testRestaurant)
            .name("대표 메뉴")
            .description("테스트용 대표 메뉴 설명")
            .price(new BigDecimal("15000.00"))
            .isBest(true)
            .build());

    // 레스토랑 운영시간 생성 (월요일만 예시)
    hoursRepository.save(
        RestaurantOperatingHours.builder()
            .restaurant(testRestaurant)
            .dayOfWeek(DayOfWeekEnum.MON)
            .openTime(LocalTime.of(10, 0))
            .closeTime(LocalTime.of(22, 0))
            .isHoliday(false)
            .build());

    userRepository.flush();
    restaurantRepository.flush();
    hoursRepository.flush();
    menuRepository.flush();
    seatRepository.flush();
  }

  @AfterEach
  void tearDown() {
    cleanUpDatabase();
  }

  void cleanUpDatabase() {
    // 레스토랑 관련 데이터 삭제
    menuRepository.deleteAllInBatch(menuRepository.findAllByRestaurantId(testRestaurant.getId()));
    hoursRepository.deleteAllInBatch(hoursRepository.findAllByRestaurantId(testRestaurant.getId()));
    seatRepository.deleteAllInBatch(seatRepository.findAllByRestaurantId(testRestaurant.getId()));
    restaurantRepository.deleteById(testRestaurant.getId());

    // 생성된 유저만 삭제
    userRepository.deleteAllInBatch(Arrays.asList(testUsers));
    userRepository.deleteAllInBatch(userRepository.findAllByUserIDIn(Arrays.asList("admin_user", "owner_user")));

    userRepository.deleteByUserID("admin_user");
    userRepository.deleteByUserID("owner_user");

    // flush()를 한 번에 실행
    userRepository.flush();
    menuRepository.flush();
    hoursRepository.flush();
    restaurantRepository.flush();
    seatRepository.flush();
  }

  @Test
  void ReservationSyncTest() throws InterruptedException {

    Arrays.stream(testUsers)
        .toList()
        .forEach(user -> System.out.println("User ID ::::::::::::" + user.getId()));

    int threadCount = 10;
    CountDownLatch latch = new CountDownLatch(threadCount);
    List<Reservation> reservationList = Collections.synchronizedList(new ArrayList<>());
    ExecutorService executor = Executors.newFixedThreadPool(threadCount);

    for (int i = 0; i < threadCount; i++) {
      int userIndex = i;
      executor.submit(
          () -> {
            try {
              ReservationRequestCommand command = new ReservationRequestCommand();
              command.setRestaurantId(testRestaurant.getId());
              command.setSeatId(testSeat.getSeatId());
              command.setUserId(testUsers[userIndex].getId());
              command.setReservationDate(reservationDate);
              command.setReservationTime(reservationTime);

              Reservation reservation = reservationService.createReservation(command);
              reservationList.add(reservation);
              System.out.println("예약 성공 - user_" + (userIndex + 1));
            } catch (ApiException e) {
              System.out.println("예약 실패 - user_" + (userIndex + 1) + ": " + e.getMessage());
            } finally {
              latch.countDown();
            }
          });
    }

    latch.await();
    executor.shutdown();

    assertEquals(1, reservationList.size(), "예약 중복 발생!");

    Reservation confirmedReservation = reservationList.get(0);

    assertEquals(reservationDate, confirmedReservation.getReservationDate());
    assertEquals(reservationTime, confirmedReservation.getReservationTime());
    reservationJpaRepository.deleteById(confirmedReservation.getReservationId());
  }

  @Test
  void ReservationCreateAndConfirmAndCancel() {
    ReservationRequestCommand command = new ReservationRequestCommand();
    command.setRestaurantId(1L);
    command.setSeatId(1L);
    command.setRestaurantId(1L);
    command.setUserId(1L);
    command.setReservationDate(reservationDate);
    command.setReservationTime(reservationTime);

    Reservation reservation = reservationService.createReservation(command);
    assertNotNull(reservation);

    reservationService.confirmReservation(reservation.getReservationId());
    assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());

    reservationService.cancelReservation(reservation.getReservationId());
    assertEquals(ReservationStatus.CANCELED, reservation.getStatus());
  }

  @Test
  void ReservationRefundCalTest() {
    // given
    Reservation reservation = new Reservation();
    //        reservation.setReservationId(1L);

    Restaurant restaurant = new Restaurant();
    //        restaurant.setId(1L);
    //        reservation.setRestaurant(restaurant);

    //        reservation.setDepositAmount(BigDecimal.valueOf(10000)); // 예약금 10,000원
    //        reservation.setReservationDate(LocalDate.now().plusDays(10)); // 10일 후 예약일

    // 환불 정책 설정 (7일 전 50%, 3일 전 30%)
    //        when(refundService.findByRestaurantId(1L)).thenReturn(List.of(
    //                new RefundPolicy(7, BigDecimal.valueOf(50)),
    //                new RefundPolicy(3, BigDecimal.valueOf(30))
    //        ));

    // when
    //        reservationService.applyRefundPolicy(reservation); // 환불금액 계산 및 reservation에 저장

    // then
    assertEquals(new BigDecimal("5000.0"), reservation.getRefundAmount()); // 50% 환불 적용
  }

  @Test
  void ReservationRedisKeyTest() {
    ReservationRequestCommand command = new ReservationRequestCommand();
    command.setRestaurantId(1L);
    command.setSeatId(2L);
    command.setUserId(1L);
    command.setReservationDate(reservationDate);
    command.setReservationTime(reservationTime);

    Reservation reservation = reservationService.createReservation(command);

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

    // Redis에 키 존재 여부 확인
    assertTrue(redisTemplate.hasKey(seatKey));
    assertTrue(redisTemplate.hasKey(userKey));

    reservationService.cancelReservation(reservation.getReservationId()); // 예약 취소

    // 예약 취소 후 Redis 키 삭제 확인
    assertFalse(redisTemplate.hasKey(seatKey));
    assertFalse(redisTemplate.hasKey(userKey));
  }
}
