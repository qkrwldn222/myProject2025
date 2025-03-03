package com.reservation.application.store.repository;




import com.reservation.domain.FoodStore;

import java.util.List;

public interface FoodStoreExternalRepository {
    List<FoodStore> fetchFoodStores();

}
