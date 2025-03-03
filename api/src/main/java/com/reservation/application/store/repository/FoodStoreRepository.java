package com.reservation.application.store.repository;


import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.domain.FoodStore;

import java.util.List;


public interface FoodStoreRepository {
    void bulkInsert(List<FoodStore> stores);
    void bulkUpdate(List<FoodStore> stores);
    List<FoodStore> fetchFoodStores(FoodStoreSearchCommand command);
}
