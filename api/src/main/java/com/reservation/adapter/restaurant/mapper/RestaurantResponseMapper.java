package com.reservation.adapter.restaurant.mapper;

import com.reservation.adapter.restaurant.model.RestaurantSearchRegistrationsResponse;
import com.reservation.application.restaurant.restaurant.model.RestaurantDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RestaurantResponseMapper {
    RestaurantResponseMapper INSTANCE = Mappers.getMapper(RestaurantResponseMapper.class);


    RestaurantSearchRegistrationsResponse toRestaurantSearchRegistrationsResponse(RestaurantDetailResponse response);

    List<RestaurantSearchRegistrationsResponse> toRestaurantSearchRegistrationsResponses(List<RestaurantDetailResponse> responses);

}
