package com.reservation.infrastructure.store;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.domain.FoodStore;
import com.reservation.application.store.repository.FoodStoreRepository;
import com.reservation.infrastructure.store.mybatis.FoodStoreMybatisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodStoreRestRepository implements FoodStoreRepository {

    private final FoodStoreMybatisRepository foodStoreMybatisRepository;

    @Override
    public void bulkInsert(List<FoodStore> stores) {
        foodStoreMybatisRepository.bulkInsert(stores);
    }

    @Override
    public void bulkUpdate(List<FoodStore> stores) {
        foodStoreMybatisRepository.bulkUpdate(stores);
    }

    @Override
    public List<FoodStore> fetchFoodStores(FoodStoreSearchCommand command) {
        return foodStoreMybatisRepository.searchFoodStores(command);
    }
}
