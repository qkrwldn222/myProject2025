package com.reservation.application.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAppMapper {
    // DI 없이 직접 객체 생성
    UserAppMapper INSTANCE = Mappers.getMapper(UserAppMapper.class);

}
