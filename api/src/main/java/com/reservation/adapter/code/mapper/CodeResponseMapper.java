package com.reservation.adapter.code.mapper;

import com.reservation.adapter.code.model.GroupCodeAdaptSearchResponse;
import com.reservation.application.code.model.GroupCodeSearchResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CodeResponseMapper {
  CodeResponseMapper INSTANCE = Mappers.getMapper(CodeResponseMapper.class);

  GroupCodeAdaptSearchResponse toGroupCodeSearchAdaptResponse(
      GroupCodeSearchResponse groupCodeSearchResponse);

  List<GroupCodeAdaptSearchResponse> toGroupCodeSearchAdaptResponses(
      List<GroupCodeSearchResponse> groupCodeSearchResponses);
}
