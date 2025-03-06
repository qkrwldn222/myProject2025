package com.reservation.application.code.mapper;

import com.reservation.application.code.model.GroupCodeSearchCommand;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CodeAppRequestMapper {
  CodeAppRequestMapper INSTANCE = Mappers.getMapper(CodeAppRequestMapper.class);

  GroupCodeDTO toGroupCodeDTO(GroupCodeSearchCommand command);
}
