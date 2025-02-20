package com.example.core.application.store.infrastructure.repository;


import com.example.core.application.store.domain.FoodStore;
import com.example.core.application.store.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class FoodStoreRestRepository implements FoodStoreRepository {
    private final StoreMybatisRepository storeMybatisRepository;

    @Override
    public void bulkInsert(List<FoodStore> stores) {
        storeMybatisRepository.bulkInsert(stores);
    }

    @Override
    public void bulkUpdate(List<FoodStore> stores) {
        storeMybatisRepository.bulkUpdate(stores);
    }
}
