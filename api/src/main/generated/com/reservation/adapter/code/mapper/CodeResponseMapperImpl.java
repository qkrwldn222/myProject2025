package com.reservation.adapter.code.mapper;

import com.reservation.adapter.code.model.GroupCodeAdaptSearchResponse;
import com.reservation.application.code.model.GroupCodeSearchResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class CodeResponseMapperImpl implements CodeResponseMapper {

  @Override
  public GroupCodeAdaptSearchResponse toGroupCodeSearchAdaptResponse(
      GroupCodeSearchResponse groupCodeSearchResponse) {
    if (groupCodeSearchResponse == null) {
      return null;
    }

    GroupCodeAdaptSearchResponse groupCodeAdaptSearchResponse = new GroupCodeAdaptSearchResponse();

    groupCodeAdaptSearchResponse.setGroupCode(groupCodeSearchResponse.getGroupCode());
    groupCodeAdaptSearchResponse.setUseYn(groupCodeSearchResponse.getUseYn());
    groupCodeAdaptSearchResponse.setDescription(groupCodeSearchResponse.getDescription());
    groupCodeAdaptSearchResponse.setId(groupCodeSearchResponse.getId());

    return groupCodeAdaptSearchResponse;
  }

  @Override
  public List<GroupCodeAdaptSearchResponse> toGroupCodeSearchAdaptResponses(
      List<GroupCodeSearchResponse> groupCodeSearchResponses) {
    if (groupCodeSearchResponses == null) {
      return null;
    }

    List<GroupCodeAdaptSearchResponse> list =
        new ArrayList<GroupCodeAdaptSearchResponse>(groupCodeSearchResponses.size());
    for (GroupCodeSearchResponse groupCodeSearchResponse : groupCodeSearchResponses) {
      list.add(toGroupCodeSearchAdaptResponse(groupCodeSearchResponse));
    }

    return list;
  }
}
