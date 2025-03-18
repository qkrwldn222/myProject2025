package com.reservation.application.reservation;

import static org.junit.jupiter.api.Assertions.*;

import com.reservation.adapter.security.config.CustomUserDetails;
import com.reservation.application.refund.service.RefundService;
import com.reservation.application.reservation.model.ReservationRequestCommand;
import com.reservation.application.reservation.service.ReservationRestService;
import com.reservation.application.restaurant.restaurant.model.RestaurantUpdateCommand;
import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.common.enums.*;
import com.reservation.domain.*;
import com.reservation.infrastructure.reservation.repository.ReservationJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantMenuSpringDataJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantOperatingHoursJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantSeatSpringDataJpaRepository;
import com.reservation.infrastructure.restaurant.restaurant.repository.RestaurantSpringDataJpaRepository;
import com.reservation.infrastructure.role.repository.RoleJpaRepositoryAdapter;
import com.reservation.infrastructure.user.repository.UserJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  @Autowired private RestaurantSpringDataJpaRepository restaurantRepository;

  @Autowired private RestaurantSeatSpringDataJpaRepository seatRepository;

  @Autowired private RestaurantMenuSpringDataJpaRepository menuRepository;

  @Autowired private RestaurantOperatingHoursJpaRepository hoursRepository;

  @Autowired private RoleJpaRepositoryAdapter roleJpaRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  private final ExecutorService executor = Executors.newFixedThreadPool(10);

  private final LocalDate reservationDate = LocalDate.now().plusDays(2);
  private final LocalTime reservationTime = LocalTime.of(12, 0);

  User[] testUsers = new User[10];
  User ownerUser;
  Restaurant testRestaurant;
  RestaurantSeat testSeat;
  @Autowired private ReservationJpaRepository reservationJpaRepository;

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
                  .password(passwordEncoder.encode("1234"))
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
                .userID("admin_user1")
                .username("admin_user1")
                .password(passwordEncoder.encode("12345"))
                .role(
                    all.stream()
                        .filter(role -> role.getRoleType().equals(RoleType.ADMIN))
                        .findFirst()
                        .orElseThrow(() -> new ApiException("사용자를 찾을 수 없습니다: ")))
                .build());

    // 가게 운영자 유저 생성
    ownerUser =
        userRepository.save(
            User.builder()
                .userID("owner_user1")
                .username("owner_user1")
                .password(passwordEncoder.encode("12345"))
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
                .ownerId(ownerUser.getId())
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
            .price(BigDecimal.ZERO)
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
    restaurantRepository.deleteAllInBatch(Collections.singletonList(testRestaurant));

    // 생성된 유저만 삭제
    userRepository.deleteAllInBatch(Arrays.asList(testUsers));
    userRepository.deleteAllInBatch(
        userRepository.findAllByUserIDIn(Arrays.asList("admin_user1", "owner_user1")));

    // flush()를 한 번에 실행
    /*  userRepository.flush();
    menuRepository.flush();
    hoursRepository.flush();
    restaurantRepository.flush();
    seatRepository.flush();*/
  }

  /** Given: 여러 사용자가 동시에 같은 좌석을 예약할 때, When: 10개의 동시 요청을 실행하면, Then: 단 하나의 예약만 성공해야 한다. */
  @Test
  void ReservationSyncTest() throws InterruptedException {
    // Given: 모든 테스트 유저 정보
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
              // When: 여러 사용자가 동시에 예약을 시도
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
    // Then: 단 하나의 예약만 성공해야 함 (중복 예약 방지)
    assertEquals(1, reservationList.size(), "예약 중복 발생!");

    Reservation confirmedReservation = reservationList.get(0);

    assertEquals(reservationDate, confirmedReservation.getReservationDate());
    assertEquals(reservationTime, confirmedReservation.getReservationTime());
    // 데이터 정리
    reservationJpaRepository.deleteById(confirmedReservation.getReservationId());
  }

  /**
   * Given: 특정 사용자가 예약을 생성할 때, When: 가게 운영자가 예약을 승인하고 이후 취소하면, Then: 예약 상태가 CONFIRMED → CANCELED 로
   * 변경되어야 한다.
   */
  @Test
  @Transactional
  void ReservationCreateAndConfirmAndCancel() {
    // Given: 로그인 유저 생성 (가게 운영자로 로그인)
    CustomUserDetails userDetails = new CustomUserDetails(ownerUser);
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);

    System.out.println("ownerUser.getUserID() :::::" + ownerUser.getUserID());

    ReservationRequestCommand command = new ReservationRequestCommand();
    command.setRestaurantId(testRestaurant.getId());
    command.setSeatId(testSeat.getSeatId());
    command.setUserId(testUsers[0].getId());
    command.setReservationDate(reservationDate);
    command.setReservationTime(reservationTime);

    // When: 예약 생성
    Reservation reservation = reservationService.createReservation(command);
    assertNotNull(reservation);

    // When: 예약 승인
    reservationService.confirmReservation(reservation.getReservationId());

    // Then: 예약 상태가 CONFIRMED 이어야 함
    assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());

    // When: 예약 취소
    reservationService.cancelReservation(reservation.getReservationId());

    // Then: 예약 상태가 CANCELED 이어야 함
    assertEquals(ReservationStatus.CANCELED, reservation.getStatus());

    // 데이터 정리
    reservationJpaRepository.deleteById(reservation.getReservationId());
    SecurityContextHolder.clearContext();
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

  /**
   * Given: 특정 사용자가 예약을 생성할 때, When: Redis에 좌석 및 사용자 예약 정보가 저장되고, Then: 예약 취소 후 Redis 키가 삭제되어야 한다.
   */
  @Test
  @Transactional
  void ReservationRedisKeyTest() {
    // Given: 로그인 유저 생성 (Spring Security가 owner_user를 현재 로그인된 사용자로 인식)
    CustomUserDetails userDetails = new CustomUserDetails(ownerUser);
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);

    ReservationRequestCommand command = new ReservationRequestCommand();
    command.setRestaurantId(testRestaurant.getId());
    command.setSeatId(testSeat.getSeatId());
    command.setUserId(testUsers[0].getId());
    command.setReservationDate(reservationDate);
    command.setReservationTime(reservationTime);

    // When: 예약을 생성하고 Redis에 키가 저장됨
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

    // Then: Redis에 예약 정보 키가 존재해야 함
    assertTrue(redisTemplate.hasKey(seatKey));
    assertTrue(redisTemplate.hasKey(userKey));

    // When: 예약 취소
    reservationService.cancelReservation(reservation.getReservationId());

    // Then: 예약 취소 후 Redis 키가 삭제되어야 함
    assertFalse(redisTemplate.hasKey(seatKey));
    assertFalse(redisTemplate.hasKey(userKey));

    // 데이터 정리
    reservationJpaRepository.deleteById(reservation.getReservationId());
  }

  /**
   * Given: 특정 사용자가 예약을 생성하고 승인 대기 시간이 지나 만료된 경우, When: 시스템이 예약 만료 취소 메서드를 실행하면, Then: 예약 상태가
   * CANCELED 로 변경되어야 한다.
   */
  @Test
  @Transactional
  void cancelExpiredReservationsTest() {
    // Given: 예약 생성 (유효한 예약) / 가게에서 자동승인 옵션 끄기
    RestaurantUpdateCommand restaurantUpdateCommand = new RestaurantUpdateCommand();
    restaurantUpdateCommand.setAutoConfirm(false);
    testRestaurant.update(restaurantUpdateCommand);
    restaurantRepository.save(testRestaurant);

    ReservationRequestCommand command = new ReservationRequestCommand();
    command.setRestaurantId(testRestaurant.getId());
    command.setSeatId(testSeat.getSeatId());
    command.setUserId(testUsers[0].getId());
    command.setReservationDate(reservationDate);
    command.setReservationTime(reservationTime);
    Reservation reservation = reservationService.createReservation(command);

    // Given: 예약 승인 대기 시간 설정 (즉시 만료되도록 설정)
    reservation.setExpiresAt(LocalDateTime.now().minusMinutes(1));
    reservationJpaRepository.save(reservation);

    // Then: 예약이 PENDING 상태인지 확인
    assertEquals(ReservationStatus.PENDING, reservation.getStatus());

    // When: 예약 만료 취소 메서드 실행 (강제 호출)
    reservationService.cancelExpiredReservations();

    // Then: 변경된 상태 확인 (CANCELED로 변경되어야 함)
    Reservation updatedReservation =
        reservationJpaRepository
            .findById(reservation.getReservationId())
            .orElseThrow(() -> new ApiException("예약을 찾을 수 없습니다."));
    assertEquals(ReservationStatus.CANCELED, updatedReservation.getStatus());

    // 데이터 정리
    reservationJpaRepository.deleteById(reservation.getReservationId());
  }
}
