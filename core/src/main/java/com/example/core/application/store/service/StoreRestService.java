package com.example.core.application.store.service;


import com.example.core.application.store.domain.FoodStore;
import com.example.core.application.store.repository.FoodStoreExternalRepository;
import com.example.core.application.store.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreRestService implements StoreService {

    private final FoodStoreRepository foodStoreRepository;
    private final FoodStoreExternalRepository foodStoreExternalRepository;

    @Override
    @Transactional
    public void foodBulkInsert() {
        List<FoodStore> foodStores = foodStoreExternalRepository.searchFoodStore();
        //Update
        List<FoodStore> updateList = foodStores.stream().filter(store -> "U".equals(store.getUpdateGbn())).toList();
        //Insert
        List<FoodStore> createList = foodStores.stream().filter(store -> "I".equals(store.getUpdateGbn())).toList();

        foodStoreRepository.bulkInsert(createList);
        foodStoreRepository.bulkInsert(createList);

    }


}
