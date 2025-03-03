package com.reservation.adapter.store.mapper;

import com.reservation.adapter.store.model.FoodStoreSearchResponse;
import com.reservation.domain.FoodStore;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class FoodStoreResponseMapperImpl implements FoodStoreResponseMapper {

  @Override
  public FoodStoreSearchResponse toFoodStoreSearchResponse(FoodStore foodStore) {
    if (foodStore == null) {
      return null;
    }

    FoodStoreSearchResponse foodStoreSearchResponse = new FoodStoreSearchResponse();

    foodStoreSearchResponse.setHomePage(foodStore.getHomePage());
    foodStoreSearchResponse.setBplcNm(foodStore.getBplcNm());
    foodStoreSearchResponse.setRdnWhlAddr(foodStore.getRdnWhlAddr());
    foodStoreSearchResponse.setRdnPostNo(foodStore.getRdnPostNo());
    foodStoreSearchResponse.setSiteTel(foodStore.getSiteTel());
    foodStoreSearchResponse.setMgtNo(foodStore.getMgtNo());

    return foodStoreSearchResponse;
  }

  @Override
  public List<FoodStoreSearchResponse> toFoodStoreSearchResponses(List<FoodStore> foodStore) {
    if (foodStore == null) {
      return null;
    }

    List<FoodStoreSearchResponse> list = new ArrayList<FoodStoreSearchResponse>(foodStore.size());
    for (FoodStore foodStore1 : foodStore) {
      list.add(toFoodStoreSearchResponse(foodStore1));
    }

    return list;
  }
}
