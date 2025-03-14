package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.application.restaurant.restaurant.model.RestaurantRegistrationSearchCommand;
import com.reservation.application.restaurant.restaurant.repository.RestaurantRepository;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantMenuDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantOperatingHoursDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantRegistrationDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantSeatDTO;
import com.reservation.infrastructure.restaurant.restaurant.mybatis.RestaurantMybatisRepository;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

@Repository
@RequiredArgsConstructor
public class RestaurantRestRepository implements RestaurantRepository {

  private final RestaurantMybatisRepository restaurantMybatisRepository;

  @Override
  public List<RestaurantRegistrationDTO> searchRegistrations(
      RestaurantRegistrationSearchCommand command) {
    return restaurantMybatisRepository.searchRegistrations(command);
  }

  @Override
  public List<RestaurantMenuDTO> findMenusByRestaurantId(List<Long> restaurantIds) {

    return CollectionUtils.isEmpty(restaurantIds)
        ? Collections.emptyList()
        : restaurantMybatisRepository.findMenusByRestaurantIds(restaurantIds);
  }

  @Override
  public List<RestaurantOperatingHoursDTO> findOperatingHoursByRestaurantId(
      List<Long> restaurantIds) {
    return CollectionUtils.isEmpty(restaurantIds)
        ? Collections.emptyList()
        : restaurantMybatisRepository.findOperatingHoursByRestaurantIds(restaurantIds);
  }

  @Override
  public List<RestaurantSeatDTO> findSeatsByRestaurantId(List<Long> restaurantIds) {
    return CollectionUtils.isEmpty(restaurantIds)
        ? Collections.emptyList()
        : restaurantMybatisRepository.findSeatsByRestaurantIds(restaurantIds);
  }
}
