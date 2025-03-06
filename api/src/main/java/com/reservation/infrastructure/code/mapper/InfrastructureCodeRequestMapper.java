package com.reservation.infrastructure.code.mapper;

import com.reservation.application.code.model.GroupCodeSaveCommand;
import com.reservation.application.code.model.GroupCodeSearchCommand;
import com.reservation.application.code.model.GroupCodeUpdateCommand;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper
public interface InfrastructureCodeRequestMapper {
  InfrastructureCodeRequestMapper INSTANCE =
      Mappers.getMapper(InfrastructureCodeRequestMapper.class);

  GroupCodeDTO toGroupCodeDTO(GroupCodeSearchCommand command);

  //  @Mapping(target = "createAt", expression = "java(java.time.LocalDateTime.now())") or xml 에서
  @Mapping(target = "createBy", expression = "java(getCurrentUsername())")
  @Mapping(target = "updateBy", expression = "java(getCurrentUsername())")
  GroupCodeDTO toGroupCodeDTO(GroupCodeSaveCommand command);

  @Mapping(target = "updateBy", expression = "java(getCurrentUsername())")
  @Mapping(target = "createBy", expression = "java(getCurrentUsername())")
  GroupCodeDTO toGroupCodeDTO(GroupCodeUpdateCommand command);

  default String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      return authentication.getName();
    }
    return "SYSTEM"; // 인증 정보가 없을 경우 기본값 설정
  }
}
