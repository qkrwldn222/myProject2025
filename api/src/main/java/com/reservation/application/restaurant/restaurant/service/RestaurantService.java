package com.reservation.application.restaurant.restaurant.service;

import com.reservation.application.restaurant.restaurant.model.*;
import com.reservation.common.enums.DayOfWeekEnum;
import com.reservation.domain.Restaurant;
import com.reservation.domain.RestaurantOperatingHours;
import com.reservation.domain.RestaurantSeat;
import java.util.List;
import java.util.Optional;

public interface RestaurantService {

  /**
   * 가게 통합 등록 메소드
   *
   * @param command 가게 통합등록 command
   * @return restaurantId
   */
  Long createRestaurant(RestaurantCreateCommand command);

  /**
   * 가게 기본정보 수정
   *
   * @param command 가게 정보 수정 command
   */
  void updateRestaurant(RestaurantUpdateCommand command);

  /**
   * 가게 삭제 (실제 삭제되지 않고 restaurant state 영업종료, restaurant_registration 퍼즈 상태로 변경
   *
   * @param restaurantId 가게 id
   */
  void deleteRestaurant(Long restaurantId);

  /**
   * 가게 좌석 생성, 수정, 삭제 일괄 처리
   *
   * @param restaurantId 가게 id
   * @param command 생성 command
   */
  void operateSeats(Long restaurantId, RestaurantSeatRequestCommand command);

  /**
   * 가게 메뉴 생성, 수정, 삭제 일괄 처리
   *
   * @param restaurantId 가게 id
   * @param command 생성 command
   */
  void operateMenus(Long restaurantId, RestaurantMenuOperateCommand command);

  /**
   * 가게 메뉴 생성, 수정, 삭제 일괄 처리
   *
   * @param restaurantId 가게 id
   * @param command 생성 command
   */
  void operateOperatingHours(Long restaurantId, RestaurantOperatingHoursOperateCommand command);

  /**
   * 가게 입점 승인
   *
   * @param command id, state, reason
   */
  void approveRestaurant(RestaurantApprovalCommand command);

  /**
   * 가게 정보 조회 (가게 운영자, 관리자 전용)
   *
   * @param command 가게id, 입점 상태 , 영업 상태, 가게명
   * @return 가게 정보
   */
  List<RestaurantDetailResponse> searchRegistrations(RestaurantRegistrationSearchCommand command);

  /**
   * 유저 id로 가게 찾기
   *
   * @param id user테이블 id
   * @return restaurant 도메인
   */
  Optional<Restaurant> findByOwnerId(Long id);

  /**
   * 가게 id로 가게 찾기
   *
   * @param restaurantId 가게 id
   * @return restaurant 도메인
   */
  Optional<Restaurant> findById(Long restaurantId);

  /**
   * 좌석id 와 가게 id로 좌석 도메인 찾기
   *
   * @param seatId 좌석 id
   * @param restaurantId 가게 id
   * @return RestaurantSeat 도메인
   */
  Optional<RestaurantSeat> findBySeatIdAndRestaurantId(Long seatId, Long restaurantId);

  Optional<RestaurantOperatingHours> findByRestaurantIdAndDayOfWeek(
      Long restaurantId, DayOfWeekEnum dayOfWeekEnum);
}
