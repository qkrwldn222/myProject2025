package com.reservation.infrastructure.store.mybatis;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.domain.FoodStore;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FoodStoreMybatisRepository {
  void bulkInsert(@Param("list") List<FoodStore> stores);

  void bulkUpdate(@Param("list") List<FoodStore> stores);

  List<FoodStore> searchFoodStores(@Param("command") FoodStoreSearchCommand command);
}
