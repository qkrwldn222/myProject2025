package com.reservation.adapter.restaurant.controller;

import com.reservation.adapter.restaurant.mapper.RestaurantRequestMapper;
import com.reservation.adapter.restaurant.mapper.RestaurantResponseMapper;
import com.reservation.adapter.restaurant.model.*;
import com.reservation.adapter.restaurant.model.RestaurantSearchRegistrationsResponse;
import com.reservation.adapter.restaurant.swagger.RestaurantSwagger;
import com.reservation.application.restaurant.restaurant.model.*;
import com.reservation.application.restaurant.restaurant.service.RestaurantService;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.common.enums.RegistrationStatus;
import com.reservation.common.enums.RestaurantStatus;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("restaurants")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
public class RestaurantController implements RestaurantSwagger {

  private final RestaurantService restaurantService;

  @Override
  @PostMapping("/operate")
  public ResponseEntity<ApiResponseWrapper<Long>> createRestaurant(
      @RequestBody RestaurantCreateRequest request) {
    RestaurantCreateCommand command = RestaurantRequestMapper.INSTANCE.toCreateCommand(request);
    Long restaurantId = restaurantService.createRestaurant(command);
    return ResponseEntity.ok(ApiResponseWrapper.createResult("가게 등록 요청이 완료되었습니다.", restaurantId));
  }

  @Override
  @PutMapping("/{restaurantId}")
  public ResponseEntity<ApiResponseWrapper<Void>> updateRestaurant(
      @PathVariable("restaurantId") Long restaurantId, @RequestBody RestaurantUpdateRequest request) {

    RestaurantUpdateCommand command =
        RestaurantRequestMapper.INSTANCE.toUpdateCommand(restaurantId, request);
    restaurantService.updateRestaurant(command);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("가게 정보 수정이 완료되었습니다."));
  }

  @Override
  @DeleteMapping("/{restaurantId}")
  public ResponseEntity<ApiResponseWrapper<Void>> deleteRestaurant(
      @PathVariable("restaurantId") Long restaurantId) {
    restaurantService.deleteRestaurant(restaurantId);
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("가게가 삭제(운영 상태 변경)되었습니다."));
  }

  @Override
  @PostMapping("/{restaurantId}/seat/operate")
  public ResponseEntity<ApiResponseWrapper<Void>> operateSeats(
      @PathVariable("restaurantId") Long restaurantId, @RequestBody RestaurantSeatOperateRequest request) {

    RestaurantSeatRequestCommand command = RestaurantRequestMapper.INSTANCE.toCommand(request);

    restaurantService.operateSeats(restaurantId, command);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("좌석 변경이 완료되었습니다."));
  }

  @Override
  @PostMapping("/{restaurantId}/menu/operate")
  public ResponseEntity<ApiResponseWrapper<Void>> operateMenu(
      @PathVariable("restaurantId") Long restaurantId, @RequestBody RestaurantMenuOperateRequest request) {
    RestaurantMenuOperateCommand command = RestaurantRequestMapper.INSTANCE.toCommand(request);

    restaurantService.operateMenus(restaurantId, command);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("메뉴 변경이 완료되었습니다."));
  }

  @Override
  @PostMapping("/{restaurantId}/operating-Hours/operate")
  public ResponseEntity<ApiResponseWrapper<Void>> operateOperatingHours(
      @PathVariable("restaurantId") Long restaurantId,
      @RequestBody RestaurantOperatingHoursOperateRequest request) {
    RestaurantOperatingHoursOperateCommand operateCommand =
        RestaurantRequestMapper.INSTANCE.toOperateCommand(request);

    restaurantService.operateOperatingHours(restaurantId, operateCommand);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("메뉴 변경이 완료되었습니다."));
  }

  @Override
  @GetMapping("/search/registrations")
  public ResponseEntity<ApiListResponse<RestaurantSearchRegistrationsResponse>> searchRestaurantRegistrations(
          @RequestParam(name = "userId" , required = false ) String userId,
          @RequestParam(name = "name" , required = false) String name,
          @RequestParam(name = "status" , required = false ) RestaurantStatus status,
          @RequestParam(name = "managementStatus" , required = false )RegistrationStatus managementStatus) {
    RestaurantRegistrationSearchCommand command = RestaurantRequestMapper.INSTANCE.toRestaurantRegistrationSearchCommand(userId, name, status, managementStatus);

    List<RestaurantDetailResponse> restaurantDetailResponses = restaurantService.searchRegistrations(command);

    List<RestaurantSearchRegistrationsResponse> result = RestaurantResponseMapper.INSTANCE.toRestaurantSearchRegistrationsResponses(restaurantDetailResponses);

    return ResponseEntity.ok(ApiListResponse.multiResult("조회 성공",result));
  }


  @Override
  @PostMapping("/{restaurantId}/process")
  public ResponseEntity<ApiResponseWrapper<Void>> processRestaurantApproval(
          @PathVariable("restaurantId") Long restaurantId,
          @RequestParam("state") RegistrationStatus state,
          @RequestParam("rejectedReason") String rejectedReason) {

    RestaurantApprovalCommand commnad = RestaurantRequestMapper.INSTANCE.toRestaurantApprovalCommand(restaurantId, state, rejectedReason);

    restaurantService.approveRestaurant(commnad);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("상태 변경이 완료되었습니다."));
  }
}
