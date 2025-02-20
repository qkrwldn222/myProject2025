package com.example.core.application.store.infrastructure.repository;

import com.example.core.application.store.domain.FoodStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMybatisRepository {
    void bulkInsert(@Param("list") List<FoodStore> stores);
    void bulkUpdate(@Param("list") List<FoodStore> stores);
}
