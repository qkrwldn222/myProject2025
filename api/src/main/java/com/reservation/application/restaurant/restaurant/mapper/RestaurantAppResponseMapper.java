package com.reservation.application.restaurant.restaurant.mapper;

import com.reservation.application.restaurant.restaurant.model.RestaurantDetailResponse;
import com.reservation.infrastructure.restaurant.restaurant.model.*;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantAppResponseMapper {
  RestaurantAppResponseMapper INSTANCE = Mappers.getMapper(RestaurantAppResponseMapper.class);

  @Mapping(target = "reservationAvailableDays", source = "restaurant.reservationAvailableDays")
  @Mapping(target = "reservationOpenType", source = "restaurant.reservationOpenType")
  @Mapping(target = "reservationOpenDays", source = "restaurant.reservationOpenDays")
  @Mapping(target = "specialBookingDays", source = "restaurant.specialBookingDays")
  @Mapping(target = "address", source = "restaurant.address")
  @Mapping(target = "phoneNumber", source = "restaurant.phoneNumber")
  @Mapping(target = "category", source = "restaurant.category")
  @Mapping(target = "name", source = "restaurant.name")
  @Mapping(target = "status", source = "restaurant.status")
  @Mapping(target = "depositAmount", source = "restaurant.depositAmount")
  @Mapping(target = "menus", source = "menus")
  @Mapping(target = "operatingHours", source = "operatingHours")
  @Mapping(target = "seats", source = "seats")
  RestaurantDetailResponse toRestaurantDetailResponse(
      RestaurantRegistrationDTO restaurant,
      List<RestaurantMenuDTO> menus,
      List<RestaurantOperatingHoursDTO> operatingHours,
      List<RestaurantSeatDTO> seats);

  RestaurantMenuDTO toRestaurantMenuDTO(RestaurantMenuDTO dto);

  RestaurantOperatingHoursDTO toRestaurantOperatingHoursDTO(RestaurantOperatingHoursDTO dto);

  RestaurantSeatDTO toRestaurantSeatDTO(RestaurantSeatDTO dto);

  List<RestaurantMenuDTO> toRestaurantMenuDTOs(List<RestaurantMenuDTO> dto);

  List<RestaurantOperatingHoursDTO> toRestaurantOperatingHoursDTOs(
      List<RestaurantOperatingHoursDTO> dto);

  List<RestaurantSeatDTO> toRestaurantSeatDTOs(List<RestaurantSeatDTO> dto);
}
