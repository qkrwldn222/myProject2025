package com.reservation.application.store.service;



import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.application.store.repository.FoodStoreExternalRepository;
import com.reservation.application.store.repository.FoodStoreRepository;
import com.reservation.domain.FoodStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodFoodStoreRestService implements FoodStoreService {

    private final FoodStoreRepository foodStoreRepository;
    private final FoodStoreExternalRepository foodStoreExternalRepository;

    @Override
    @Transactional
    public void foodBulkInsert() {
        List<FoodStore> foodStores = foodStoreExternalRepository.fetchFoodStores();
        //Update
        List<FoodStore> updateList = foodStores.stream().filter(store -> "U".equals(store.getUpdateGbn())).toList();
        //Insert
        List<FoodStore> createList = foodStores.stream().filter(store -> "I".equals(store.getUpdateGbn())).toList();

        foodStoreRepository.bulkInsert(createList);
        foodStoreRepository.bulkInsert(updateList);
    }

    @Override
    public List<FoodStore> searchFoodStores(FoodStoreSearchCommand command) {
        return foodStoreRepository.fetchFoodStores(command);
    }
}
