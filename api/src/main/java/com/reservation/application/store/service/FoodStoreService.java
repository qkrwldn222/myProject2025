package com.reservation.application.store.service;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.domain.FoodStore;

import java.util.List;

public interface FoodStoreService {
    void foodBulkInsert();
    List<FoodStore> searchFoodStores(FoodStoreSearchCommand command);
}
