package com.reservation.adapter.code.mapper;

import com.reservation.adapter.code.model.CodeOperateRequest;
import com.reservation.application.code.model.CodeOperateCommand;
import com.reservation.application.code.model.GroupCodeSearchCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CodeRequestMapper {
  CodeRequestMapper INSTANCE = Mappers.getMapper(CodeRequestMapper.class);

  @Mapping(source = "saveRequests", target = "saveCommands")
  @Mapping(source = "updateRequests", target = "updateCommands")
  @Mapping(source = "deleteRequests", target = "deleteCommands")
  CodeOperateCommand toOperateCommand(CodeOperateRequest request);

  GroupCodeSearchCommand tosSearchGroupCodeCommand(String groupCode, String useYn);
}
