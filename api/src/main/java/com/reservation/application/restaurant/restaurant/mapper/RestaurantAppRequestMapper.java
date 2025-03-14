package com.reservation.application.restaurant.restaurant.mapper;

import com.reservation.application.restaurant.restaurant.model.RestaurantCreateCommand;
import com.reservation.application.restaurant.restaurant.model.RestaurantMenuCommand;
import com.reservation.application.restaurant.restaurant.model.RestaurantOperatingHoursCommand;
import com.reservation.application.restaurant.restaurant.model.RestaurantSeatCommand;
import com.reservation.domain.*;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantAppRequestMapper {
  RestaurantAppRequestMapper INSTANCE = Mappers.getMapper(RestaurantAppRequestMapper.class);

  Restaurant toRestaurant(RestaurantCreateCommand command);


  @Mapping(target = "restaurant", ignore = true) // Context에서 매핑
  RestaurantMenu toMenu(RestaurantMenuCommand menuCommand, @Context Restaurant restaurant);

  @Mapping(target = "restaurant", ignore = true)
  RestaurantOperatingHours toOperatingHours(
      RestaurantOperatingHoursCommand hoursCommand, @Context Restaurant restaurant);

  @Mapping(target = "restaurant", ignore = true)
  RestaurantSeat toSeat(RestaurantSeatCommand seatCommand, @Context Restaurant restaurant);

  default List<RestaurantMenu> toMenus(
      List<RestaurantMenuCommand> menus, @Context Restaurant restaurant) {
    return menus.stream().map(menuCommand -> toMenu(menuCommand, restaurant)).toList();
  }

  default List<RestaurantOperatingHours> toOperatingHours(
      List<RestaurantOperatingHoursCommand> hours, @Context Restaurant restaurant) {
    return hours.stream().map(hoursCommand -> toOperatingHours(hoursCommand, restaurant)).toList();
  }

  default List<RestaurantSeat> toSeats(
      List<RestaurantSeatCommand> seats, @Context Restaurant restaurant) {
    return seats.stream().map(seatCommand -> toSeat(seatCommand, restaurant)).toList();
  }
}
