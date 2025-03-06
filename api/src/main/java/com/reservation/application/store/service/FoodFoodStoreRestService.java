package com.reservation.application.store.service;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.application.store.repository.FoodStoreExternalRepository;
import com.reservation.application.store.repository.FoodStoreRepository;
import com.reservation.domain.FoodStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodFoodStoreRestService implements FoodStoreService {

  private final FoodStoreRepository foodStoreRepository;
  private final FoodStoreExternalRepository foodStoreExternalRepository;

  @Override
  @Transactional
  public void foodBulkInsert() {
    List<FoodStore> foodStores = foodStoreExternalRepository.fetchFoodStores();
    // Update
    List<FoodStore> updateList =
        foodStores.stream().filter(store -> "U".equals(store.getUpdateGbn())).toList();
    // Insert
    List<FoodStore> createList =
        foodStores.stream().filter(store -> "I".equals(store.getUpdateGbn())).toList();

    bulkInsertInBatches(createList);
    bulkUpdateInBatches(updateList);
  }

  @Override
  public List<FoodStore> searchFoodStores(FoodStoreSearchCommand command) {
    return foodStoreRepository.fetchFoodStores(command);
  }

  // 1,000개씩 끊어서 Insert 실행
  private void bulkInsertInBatches(List<FoodStore> foodStores) {
    int batchSize = 1000;
    for (int i = 0; i < foodStores.size(); i += batchSize) {
      int endIndex = Math.min(i + batchSize, foodStores.size());
      List<FoodStore> batchList = foodStores.subList(i, endIndex);
      foodStoreRepository.bulkInsert(batchList);
    }
  }

  // 1,000개씩 끊어서 Update 실행
  private void bulkUpdateInBatches(List<FoodStore> foodStores) {
    int batchSize = 1000;
    for (int i = 0; i < foodStores.size(); i += batchSize) {
      int endIndex = Math.min(i + batchSize, foodStores.size());
      List<FoodStore> batchList = foodStores.subList(i, endIndex);
      foodStoreRepository.bulkUpdate(batchList);
    }
  }
}
