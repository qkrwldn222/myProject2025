package com.reservation.adapter.menu.mapper;

import com.reservation.adapter.menu.model.MenuCreateRequest;
import com.reservation.adapter.menu.model.MenuDeleteRequest;
import com.reservation.adapter.menu.model.MenuOperateRequest;
import com.reservation.adapter.menu.model.MenuUpdateRequest;
import com.reservation.application.menu.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MenuRequestMapper {

   MenuRequestMapper INSTANCE = Mappers.getMapper(MenuRequestMapper.class);

   MenuSearchCommand toSearchCommand(String name, String code, String roleName);

   // `MenuCreateRequest` → `MenuSaveCommand` 매핑 명시적으로 추가
   @Mapping(source = "createRequests", target = "saveList")
   @Mapping(source = "updateRequests", target = "updateList")
   @Mapping(source = "deleteRequests", target = "deleteList")
   MenuOperateCommand toOperateCommand(MenuOperateRequest request);

   List<MenuSaveCommand> toSaveCommandList(List<MenuCreateRequest> createRequests);
   List<MenuUpdateCommand> toUpdateCommandList(List<MenuUpdateRequest> updateRequests);
   List<MenuDeleteCommand> toDeleteCommandList(List<MenuDeleteRequest> deleteRequests);


   MenuSaveCommand toSaveCommand(MenuCreateRequest createRequest);
   MenuUpdateCommand toUpdateCommand(MenuUpdateRequest updateRequest);

}
