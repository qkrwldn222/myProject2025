package com.reservation.adapter.security.mapper;


import com.reservation.adapter.security.model.SignupRequest;
import com.reservation.application.user.model.SignupCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SecurityRequestMapper {
    SecurityRequestMapper INSTANCE = Mappers.getMapper(SecurityRequestMapper.class);

    SignupCommand toCommand(SignupRequest request);
}
