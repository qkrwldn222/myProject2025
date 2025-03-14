package com.reservation.adapter.restaurant.mapper;

import com.reservation.adapter.restaurant.model.*;
import com.reservation.application.restaurant.restaurant.model.*;
import java.util.List;

import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.RestaurantStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RestaurantRequestMapper {
  RestaurantRequestMapper INSTANCE = Mappers.getMapper(RestaurantRequestMapper.class);

  RestaurantCreateCommand toCreateCommand(RestaurantCreateRequest request);

  RestaurantUpdateCommand toUpdateCommand(Long restaurantId, RestaurantUpdateRequest request);

  List<RestaurantMenuCommand> toMenuCommands(List<RestaurantMenuRequest> menus);

  List<RestaurantOperatingHoursCommand> toOperatingHoursCommands(
      List<RestaurantOperatingHoursRequest> operatingHours);

  List<RestaurantSeatCommand> toSeatCommands(List<RestaurantSeatRequest> seats);

  List<RestaurantSeatUpdateCommand> toSeatUpdateCommands(List<RestaurantSeatUpdateRequest> seats);

  @Mapping(target = "createCommands", source = "createRequests")
  @Mapping(target = "updateCommands", source = "updateRequests")
  @Mapping(target = "deleteCommands", source = "deleteRequests")
  RestaurantSeatRequestCommand toCommand(RestaurantSeatOperateRequest request);

  List<RestaurantMenuCreateCommand> toCreateCommands(List<RestaurantMenuCreateRequest> requests);

  List<RestaurantMenuUpdateCommand> toUpdateCommands(List<RestaurantMenuUpdateRequest> requests);

  @Mapping(target = "createCommands", source = "createRequests")
  @Mapping(target = "updateCommands", source = "updateRequests")
  @Mapping(target = "deleteCommands", source = "deleteRequests")
  RestaurantMenuOperateCommand toCommand(RestaurantMenuOperateRequest request);

  List<RestaurantOperatingHoursCreateCommand> toOperatingHoursCreateCommands(
      List<RestaurantOperatingHoursCreateRequest> requests);

  List<RestaurantOperatingHoursUpdateCommand> toOperatingHoursUpdateCommands(
      List<RestaurantOperatingHoursUpdateRequest> requests);

  @Mapping(target = "createCommands", source = "createRequests")
  @Mapping(target = "updateCommands", source = "updateRequests")
  @Mapping(target = "deleteCommands", source = "deleteRequests")
  RestaurantOperatingHoursOperateCommand toOperateCommand(
      RestaurantOperatingHoursOperateRequest request);


  RestaurantApprovalCommand toRestaurantApprovalCommand(Long restaurantId, RegistrationStatus state,String rejectedReason);

  RestaurantRegistrationSearchCommand toRestaurantRegistrationSearchCommand(String userId,
   String name,
   RestaurantStatus status,
   RegistrationStatus managementStatus);
}
