package com.reservation.adapter.store.mapper;

import com.reservation.application.store.model.FoodStoreSearchCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoodStoreRequestMapper {
    FoodStoreRequestMapper INSTANCE = Mappers.getMapper(FoodStoreRequestMapper.class);

    FoodStoreSearchCommand toStoreSearchCommand(String mgtNo, String bplcNm, String rdnWhlAddr );
}
