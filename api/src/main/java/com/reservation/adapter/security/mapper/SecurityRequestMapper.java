package com.reservation.adapter.security.mapper;


import com.reservation.adapter.security.model.SignupRequest;
import com.reservation.application.user.model.SignupCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SecurityRequestMapper {
    // DI 없이 직접 객체 생성
    SecurityRequestMapper INSTANCE = Mappers.getMapper(SecurityRequestMapper.class);

    SignupCommand toCommand(SignupRequest request);
}
