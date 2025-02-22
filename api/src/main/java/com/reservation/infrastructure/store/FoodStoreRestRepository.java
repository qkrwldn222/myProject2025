package com.reservation.infrastructure.store;

import com.reservation.domain.FoodStore;
import com.reservation.application.store.repository.FoodStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodStoreRestRepository implements FoodStoreRepository {

//    private final StoreMybatisRepository storeMybatisRepository;

    @Override
    public void bulkInsert(List<FoodStore> stores) {
//        storeMybatisRepository.bulkInsert(stores);
    }

    @Override
    public void bulkUpdate(List<FoodStore> stores) {
//        storeMybatisRepository.bulkUpdate(stores);
    }
}
