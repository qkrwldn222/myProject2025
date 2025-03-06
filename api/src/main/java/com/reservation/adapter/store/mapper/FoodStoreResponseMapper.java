package com.reservation.adapter.store.mapper;

import com.reservation.adapter.store.model.FoodStoreSearchResponse;
import com.reservation.domain.FoodStore;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoodStoreResponseMapper {
  FoodStoreResponseMapper INSTANCE = Mappers.getMapper(FoodStoreResponseMapper.class);

  FoodStoreSearchResponse toFoodStoreSearchResponse(FoodStore foodStore);

  List<FoodStoreSearchResponse> toFoodStoreSearchResponses(List<FoodStore> foodStore);
}
