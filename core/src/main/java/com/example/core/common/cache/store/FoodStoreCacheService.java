package com.example.core.common.cache.store;

import com.example.core.application.store.domain.FoodStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FoodStoreCacheService {

    private final FoodStoreJpaRepository foodStoreRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "foodStoreList";

    // 서버 시작 시 캐시 데이터 로드 (JPA → Redis)
    @PostConstruct
    public void loadCache() {
        reloadCache();
    }

    // 캐시에서 음식점 리스트 가져오기 (캐시에 없으면 자동 로드)
    @Cacheable(value = "foodStoreList")
    public List<FoodStore> getAllFoodStores() {
        return (List<FoodStore>) redisTemplate.opsForValue().get(CACHE_KEY);
    }

    // 특정 음식점 정보 가져오기 (캐시에 없으면 자동 로드)
    @Cacheable(value = "foodStore", key = "#mgtNo")
    public FoodStore getFoodStoreById(Long mgtNo) {
        return (FoodStore) redisTemplate.opsForHash().get("foodStore", mgtNo);
    }

    // 캐시 초기화 및 갱신 (DB에서 다시 조회 후 Redis에 저장)
    @CacheEvict(value = "foodStoreList", allEntries = true)
    public void reloadCache() {
        List<FoodStore> stores = foodStoreRepository.findAll();
        redisTemplate.opsForValue().set(CACHE_KEY, stores, 1, TimeUnit.HOURS); // 1시간 캐싱
        System.out.println("✅ Redis Cache Reloaded: " + stores.size() + " items");
    }

    // 특정 음식점 정보 삭제 (관리자 요청 시)
    @CacheEvict(value = "foodStore", key = "#mgtNo")
    public void evictFoodStore(Long mgtNo) {
        redisTemplate.opsForHash().delete("foodStore", mgtNo);
        System.out.println("❌ Cached FoodStore Removed: " + mgtNo);
    }

    // 캐시 업데이트 (DB 변경 시 Redis 캐시도 갱신)
    @CachePut(value = "foodStore", key = "#store.mgtNo")
    public FoodStore updateFoodStore(FoodStore store) {
        redisTemplate.opsForHash().put("foodStore", store.getMgtNo(), store);
        return store;
    }
}
