package com.example.core.application.store.repository;



import com.example.core.application.store.domain.FoodStore;

import java.util.List;

public interface FoodStoreExternalRepository {
    List<FoodStore> searchFoodStore();

}
