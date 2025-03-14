package com.reservation.infrastructure.restaurant.restaurant.mybatis;

import com.reservation.application.restaurant.restaurant.model.RestaurantRegistrationSearchCommand;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantMenuDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantOperatingHoursDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantRegistrationDTO;
import com.reservation.infrastructure.restaurant.restaurant.model.RestaurantSeatDTO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RestaurantMybatisRepository {

  List<RestaurantRegistrationDTO> searchRegistrations(
      @Param("command") RestaurantRegistrationSearchCommand command);

  List<RestaurantMenuDTO> findMenusByRestaurantIds(@Param("command") List<Long> restaurantIds);

  List<RestaurantOperatingHoursDTO> findOperatingHoursByRestaurantIds(
      @Param("command") List<Long> restaurantIds);

  List<RestaurantSeatDTO> findSeatsByRestaurantIds(@Param("command") List<Long> restaurantIds);
}
