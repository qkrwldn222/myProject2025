package com.example.core.application.store.repository;



import com.example.core.application.store.domain.FoodStore;

import java.util.List;


public interface FoodStoreRepository {
    void bulkInsert(List<FoodStore> stores);
    void bulkUpdate(List<FoodStore> stores);
}
