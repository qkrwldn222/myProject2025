package com.reservation.application.restaurant.restaurant.service;

import com.reservation.application.restaurant.restaurant.mapper.RestaurantAppRequestMapper;
import com.reservation.application.restaurant.restaurant.mapper.RestaurantAppResponseMapper;
import com.reservation.application.restaurant.restaurant.model.*;
import com.reservation.application.restaurant.restaurant.repository.RestaurantRepository;
import com.reservation.application.user.service.UserService;
import com.reservation.common.config.ApiException;
import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.RestaurantStatus;
import com.reservation.domain.*;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantMenuDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantOperatingHoursDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantRegistrationDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantSeatDTO;
import com.reservation.infrastructure.restaurant.restaurant.repository.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class RestaurantRestService implements RestaurantService {

  private final RestaurantJpaRepository restaurantRepository;
  private final RestaurantImageJpaRepository restaurantImageRepository;
  private final RestaurantMenuJpaRepository restaurantMenuRepository;
  private final RestaurantOperatingHoursJpaRepository restaurantOperatingHoursRepository;
  private final RestaurantSeatJpaRepository restaurantSeatRepository;
  private final RestaurantRegistrationJpaRepository restaurantRegistrationRepository;
  private final UserService userService;
  private final RestaurantRepository restaurantMybatisRepository;

  @Override
  @Transactional
  public Long createRestaurant(RestaurantCreateCommand command) {
    command.validate();

    // 가게 운영자인지 검증
    if (!userService.isOwner(command.getOwnerId())) {
      throw new ApiException("해당 사용자는 가게 운영자가 아닙니다.");
    }

    // 가게 기본 정보 저장
    Restaurant restaurant =
        Restaurant.builder()
            .name(command.getName())
            .category(command.getCategory())
            .phoneNumber(command.getPhoneNumber())
            .address(command.getAddress())
                .ownerId(command.getOwnerId())
//            .ownerUser(
//                userService
//                    .findById(command.getOwnerId())
//                    .orElseThrow(() -> new ApiException("해당 사용자를 찾을 수 없습니다.")))
            .autoConfirm(command.getAutoConfirm() != null ? command.getAutoConfirm() : false)
            .approvalTimeout(
                command.getApprovalTimeout() != null ? command.getApprovalTimeout() : 60)
            .status(RegistrationStatus.PENDING) // 기본값 설정
            .reservationOpenType(command.getReservationOpenType())
            .reservationOpenDays(command.getReservationOpenDays())
            .reservationAvailableDays(command.getReservationAvailableDays())
            .specialBookingDays(command.getSpecialBookingDays())
            .depositAmount(
                command.getDepositAmount() != null ? command.getDepositAmount() : BigDecimal.ZERO)
            .build();

    restaurantRepository.save(restaurant);

    //  메뉴 저장
    if (command.getMenus() != null && !command.getMenus().isEmpty()) {
      List<RestaurantMenu> menus =
          command.getMenus().stream()
              .map(
                  menuCmd ->
                      RestaurantMenu.builder()
                          .restaurant(restaurant)
                          .name(menuCmd.getName())
                          .description(menuCmd.getDescription())
                          .price(menuCmd.getPrice())
                          .imageUrl(menuCmd.getImageUrl())
                          .isBest(menuCmd.getIsBest() != null ? menuCmd.getIsBest() : false)
                          .build())
              .toList();
      restaurantMenuRepository.saveAll(menus);
    }

    //  운영 시간 저장
    if (command.getOperatingHours() != null && !command.getOperatingHours().isEmpty()) {
      List<RestaurantOperatingHours> hours =
          command.getOperatingHours().stream()
              .map(
                  hourCmd ->
                      RestaurantOperatingHours.builder()
                          .restaurant(restaurant)
                          .dayOfWeek(hourCmd.getDayOfWeek())
                          .openTime(hourCmd.getOpenTime())
                          .closeTime(hourCmd.getCloseTime())
                          .isHoliday(
                              hourCmd.getIsHoliday() != null ? hourCmd.getIsHoliday() : false)
                          .build())
              .toList();
      restaurantOperatingHoursRepository.saveAll(hours);
    }

    //  좌석 저장
    if (command.getSeats() != null && !command.getSeats().isEmpty()) {
      List<RestaurantSeat> seats =
          command.getSeats().stream()
              .map(
                  seatCmd ->
                      RestaurantSeat.builder()
                          .restaurant(restaurant)
                          .seatType(seatCmd.getSeatType())
                          .seatNumber(seatCmd.getSeatNumber())
                          .maxCapacity(seatCmd.getMaxCapacity())
                          .isAvailable(
                              seatCmd.getIsAvailable() != null ? seatCmd.getIsAvailable() : true)
                          .build())
              .toList();
      restaurantSeatRepository.saveAll(seats);
    }

    //  입점 요청 저장
    RestaurantRegistration registration =
        RestaurantRegistration.builder()
            .restaurant(restaurant)
            .owner(
                userService
                    .findById(command.getOwnerId())
                    .orElseThrow(() -> new ApiException("해당 사용자를 찾을 수 없습니다.")))
            .status(RegistrationStatus.PENDING)
            .build();
    restaurantRegistrationRepository.save(registration);

    return restaurant.getId();
  }

  @Override
  @Transactional
  public void updateRestaurant(RestaurantUpdateCommand command) {
    Restaurant restaurant =
        restaurantRepository
            .findById(command.getRestaurantId())
            .orElseThrow(() -> new ApiException("해당 가게를 찾을 수 없습니다."));
    restaurant.update(command);
    restaurantRepository.save(restaurant);
  }

  @Override
  @Transactional
  public void deleteRestaurant(Long restaurantId) {
    Restaurant restaurant =
        restaurantRepository
            .findById(restaurantId)
            .orElseThrow(() -> new ApiException("해당 가게를 찾을 수 없습니다."));
    restaurant.markAsDeleted(); // 상태 변경
    restaurantRepository.save(restaurant);
  }

  @Transactional
  public void operateSeats(Long restaurantId, RestaurantSeatRequestCommand command) {
    // Restaurant 객체 조회
    Restaurant restaurant =
        restaurantRepository
            .findById(restaurantId)
            .orElseThrow(() -> new ApiException("해당 레스토랑을 찾을 수 없습니다."));

    // 좌석 추가 (Builder 패턴 활용)
    if (!CollectionUtils.isEmpty((command.getCreateCommands()))) {
      List<RestaurantSeat> newSeats =
          command.getCreateCommands().stream()
              .map(
                  createCmd ->
                      RestaurantSeat.builder()
                          .restaurant(restaurant) // 🔹 Restaurant 객체 명시적으로 주입
                          .seatType(createCmd.getSeatType())
                          .seatNumber(createCmd.getSeatNumber())
                          .maxCapacity(createCmd.getMaxCapacity())
                          .isAvailable(createCmd.isAvailable())
                          .build())
              .toList();
      restaurantSeatRepository.saveAll(newSeats);
    }

    // 좌석 수정 (기존 데이터 찾은 후 업데이트)
    if (!CollectionUtils.isEmpty(command.getUpdateCommands())) {
      for (var updateCmd : command.getUpdateCommands()) {
        RestaurantSeat seat =
            restaurantSeatRepository
                .findById(updateCmd.getId())
                .orElseThrow(() -> new ApiException("해당 좌석을 찾을 수 없습니다."));

        // 엔티티 내부에서 업데이트 수행
        seat.update(updateCmd);
      }
    }

    // 좌석 삭제 (ID 리스트)
    if (!CollectionUtils.isEmpty(command.getDeleteCommands())) {
      restaurantSeatRepository.deleteAllById(command.getDeleteCommands());
    }
  }

  @Override
  @Transactional
  public void operateMenus(Long restaurantId, RestaurantMenuOperateCommand command) {
    command.validate();

    Restaurant restaurant =
        restaurantRepository
            .findById(restaurantId)
            .orElseThrow(() -> new ApiException("해당 레스토랑을 찾을 수 없습니다."));

    // 1. 메뉴 추가
    if (!CollectionUtils.isEmpty((command.getCreateCommands()))) {
      List<RestaurantMenu> newMenus =
          command.getCreateCommands().stream()
              .map(
                  createCmd ->
                      RestaurantMenu.builder()
                          .restaurant(restaurant)
                          .name(createCmd.getName())
                          .description(createCmd.getDescription())
                          .price(createCmd.getPrice())
                          .imageUrl(createCmd.getImageUrl())
                          .isBest(createCmd.getIsBest())
                          .build())
              .toList();
      restaurantMenuRepository.saveAll(newMenus);
    }

    // 2. 메뉴 수정
    if (!CollectionUtils.isEmpty((command.getUpdateCommands()))) {
      for (var updateCmd : command.getUpdateCommands()) {
        RestaurantMenu menu =
            restaurantMenuRepository
                .findById(updateCmd.getId())
                .orElseThrow(() -> new ApiException("해당 메뉴를 찾을 수 없습니다."));
        menu.update(updateCmd);
      }
    }

    // 3. 메뉴 삭제
    if (!CollectionUtils.isEmpty((command.getDeleteCommands()))) {
      restaurantMenuRepository.deleteAllById(command.getDeleteCommands());
    }
  }

  @Override
  @Transactional
  public void operateOperatingHours(
      Long restaurantId, RestaurantOperatingHoursOperateCommand command) {
    command.validate();

    Restaurant restaurant =
        restaurantRepository
            .findById(restaurantId)
            .orElseThrow(() -> new ApiException("해당 레스토랑을 찾을 수 없습니다."));

    // 운영 시간 추가
    if (!CollectionUtils.isEmpty((command.getCreateCommands()))) {
      List<RestaurantOperatingHours> newOperatingHours =
          command.getCreateCommands().stream()
              .map(
                  createCmd ->
                      RestaurantOperatingHours.builder()
                          .restaurant(restaurant)
                          .dayOfWeek(createCmd.getDayOfWeek())
                          .openTime(createCmd.getOpenTime())
                          .closeTime(createCmd.getCloseTime())
                          .isHoliday(createCmd.getIsHoliday())
                          .build())
              .toList();
      restaurantOperatingHoursRepository.saveAll(newOperatingHours);
    }

    // 운영 시간 수정
    if (!CollectionUtils.isEmpty((command.getUpdateCommands()))) {
      for (var updateCmd : command.getUpdateCommands()) {
        RestaurantOperatingHours operatingHours =
            restaurantOperatingHoursRepository
                .findById(updateCmd.getId())
                .orElseThrow(() -> new ApiException("해당 운영 시간을 찾을 수 없습니다."));
        operatingHours.update(updateCmd);
      }
    }

    // 운영 시간 삭제
    if (!CollectionUtils.isEmpty((command.getDeleteCommands()))) {
      restaurantOperatingHoursRepository.deleteAllById(command.getDeleteCommands());
    }
  }

  @Override
  @Transactional
  public void approveRestaurant(RestaurantApprovalCommand command) {
    command.validate();

    Restaurant restaurant = restaurantRepository.findById(command.getRestaurantId())
            .orElseThrow(() -> new ApiException("가게를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

    // 입점 신청 내역 조회
    RestaurantRegistration registration = restaurantRegistrationRepository.findByRestaurantId(command.getRestaurantId())
            .orElseThrow(() -> new ApiException("입점 신청 내역을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

    // 현재 상태 확인 (이미 승인/거절된 경우 예외 처리)
    if (registration.getStatus() == RegistrationStatus.APPROVED || registration.getStatus() == RegistrationStatus.REJECTED) {
      throw new ApiException("이미 처리된 가게 신청입니다.", HttpStatus.BAD_REQUEST);
    }

    // 현재 로그인한 사용자가 관리자(Admin)인지 확인
    if (!userService.isAdmin()) {
      throw new ApiException("권한이 없습니다.", HttpStatus.FORBIDDEN);
    }

    // 상태 변경 및 저장
    restaurant.updateStatus(command.getState());
    registration.updateStatus(command.getState(), StringUtils.hasText(command.getRejectedReason()) ? command.getRejectedReason() : null);
    restaurantRepository.save(restaurant);
    restaurantRegistrationRepository.save(registration);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RestaurantDetailResponse> searchRegistrations(RestaurantRegistrationSearchCommand command) {

    // 레스토랑 리스트 조회
    List<RestaurantRegistrationDTO> restaurants = restaurantMybatisRepository.searchRegistrations(command);

    //restaurant_id 목록 추출
    List<Long> restaurantIds = restaurants.stream()
            .map(RestaurantRegistrationDTO::getRestaurantId)
            .toList();

    if (restaurantIds.isEmpty()) {
      return Collections.emptyList();
    }

    //메뉴, 운영시간, 좌석 조회 (foreach 사용)
    List<RestaurantMenuDTO> menus = restaurantMybatisRepository.findMenusByRestaurantId(restaurantIds);
    List<RestaurantOperatingHoursDTO> operatingHours = restaurantMybatisRepository.findOperatingHoursByRestaurantId(restaurantIds);
    List<RestaurantSeatDTO> seats = restaurantMybatisRepository.findSeatsByRestaurantId(restaurantIds);

    // 레스토랑 리스트를 `stream`으로 돌면서 메뉴, 운영시간, 좌석 매핑
    return restaurants.stream()
            .map(restaurant -> RestaurantAppResponseMapper.INSTANCE.toRestaurantDetailResponse(
                    restaurant,
                    menus.stream().filter(menu -> menu.getRestaurantId().equals(restaurant.getRestaurantId())).toList(),
                    operatingHours.stream().filter(menu -> menu.getRestaurantId().equals(restaurant.getRestaurantId())).toList(),
                    seats.stream().filter(menu -> menu.getRestaurantId().equals(restaurant.getRestaurantId())).toList()
            ))
            .toList();
  }

}
