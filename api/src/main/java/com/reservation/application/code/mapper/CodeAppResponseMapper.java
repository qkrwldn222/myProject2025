package com.reservation.application.code.mapper;

import com.reservation.application.code.model.GroupCodeSearchResponse;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CodeAppResponseMapper {
  CodeAppResponseMapper INSTANCE = Mappers.getMapper(CodeAppResponseMapper.class);

  GroupCodeSearchResponse toGroupCodeSearchResponse(GroupCodeDTO groupCodeDTO);

  List<GroupCodeSearchResponse> toGroupCodeSearchResponses(List<GroupCodeDTO> groupCodeDTOS);
}
