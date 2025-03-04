package com.reservation.common.cache.store;


import com.reservation.domain.FoodStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodStoreJpaRepository extends JpaRepository<FoodStore, Long> {
    List<FoodStore> findAll(); // 모든 음식점 정보를 가져오는 메서드
}
