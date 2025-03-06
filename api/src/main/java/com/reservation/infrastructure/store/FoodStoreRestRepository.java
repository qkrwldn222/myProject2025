package com.reservation.infrastructure.store;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.application.store.repository.FoodStoreRepository;
import com.reservation.domain.FoodStore;
import com.reservation.infrastructure.store.mybatis.FoodStoreMybatisRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FoodStoreRestRepository implements FoodStoreRepository {

  private final FoodStoreMybatisRepository foodStoreMybatisRepository;

  @Override
  public void bulkInsert(List<FoodStore> stores) {
    foodStoreMybatisRepository.bulkInsert(stores);
  }

  @Override
  public void bulkUpdate(List<FoodStore> stores) {
    foodStoreMybatisRepository.bulkUpdate(stores);
  }

  @Override
  public List<FoodStore> fetchFoodStores(FoodStoreSearchCommand command) {
    return foodStoreMybatisRepository.searchFoodStores(command);
  }
}
