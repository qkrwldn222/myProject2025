package com.reservation.adapter.menu.mapper;

import com.reservation.adapter.menu.model.MenuCreateRequest;
import com.reservation.adapter.menu.model.MenuDeleteRequest;
import com.reservation.adapter.menu.model.MenuOperateRequest;
import com.reservation.adapter.menu.model.MenuUpdateRequest;
import com.reservation.application.menu.model.MenuDeleteCommand;
import com.reservation.application.menu.model.MenuOperateCommand;
import com.reservation.application.menu.model.MenuSaveCommand;
import com.reservation.application.menu.model.MenuSearchCommand;
import com.reservation.application.menu.model.MenuUpdateCommand;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class MenuRequestMapperImpl implements MenuRequestMapper {

  @Override
  public MenuSearchCommand toSearchCommand(String name, String code, String roleName) {
    if (name == null && code == null && roleName == null) {
      return null;
    }

    MenuSearchCommand menuSearchCommand = new MenuSearchCommand();

    menuSearchCommand.setName(name);
    menuSearchCommand.setCode(code);
    menuSearchCommand.setRoleName(roleName);

    return menuSearchCommand;
  }

  @Override
  public MenuOperateCommand toOperateCommand(MenuOperateRequest request) {
    if (request == null) {
      return null;
    }

    MenuOperateCommand menuOperateCommand = new MenuOperateCommand();

    menuOperateCommand.setSaveList(toSaveCommandList(request.createRequests()));
    menuOperateCommand.setUpdateList(toUpdateCommandList(request.updateRequests()));
    menuOperateCommand.setDeleteList(toDeleteCommandList(request.deleteRequests()));

    return menuOperateCommand;
  }

  @Override
  public List<MenuSaveCommand> toSaveCommandList(List<MenuCreateRequest> createRequests) {
    if (createRequests == null) {
      return null;
    }

    List<MenuSaveCommand> list = new ArrayList<MenuSaveCommand>(createRequests.size());
    for (MenuCreateRequest menuCreateRequest : createRequests) {
      list.add(toSaveCommand(menuCreateRequest));
    }

    return list;
  }

  @Override
  public List<MenuUpdateCommand> toUpdateCommandList(List<MenuUpdateRequest> updateRequests) {
    if (updateRequests == null) {
      return null;
    }

    List<MenuUpdateCommand> list = new ArrayList<MenuUpdateCommand>(updateRequests.size());
    for (MenuUpdateRequest menuUpdateRequest : updateRequests) {
      list.add(toUpdateCommand(menuUpdateRequest));
    }

    return list;
  }

  @Override
  public List<MenuDeleteCommand> toDeleteCommandList(List<MenuDeleteRequest> deleteRequests) {
    if (deleteRequests == null) {
      return null;
    }

    List<MenuDeleteCommand> list = new ArrayList<MenuDeleteCommand>(deleteRequests.size());
    for (MenuDeleteRequest menuDeleteRequest : deleteRequests) {
      list.add(menuDeleteRequestToMenuDeleteCommand(menuDeleteRequest));
    }

    return list;
  }

  @Override
  public MenuSaveCommand toSaveCommand(MenuCreateRequest createRequest) {
    if (createRequest == null) {
      return null;
    }

    MenuSaveCommand menuSaveCommand = new MenuSaveCommand();

    menuSaveCommand.setName(createRequest.name());
    menuSaveCommand.setCode(createRequest.code());
    menuSaveCommand.setRoleCode(createRequest.roleCode());

    return menuSaveCommand;
  }

  @Override
  public MenuUpdateCommand toUpdateCommand(MenuUpdateRequest updateRequest) {
    if (updateRequest == null) {
      return null;
    }

    MenuUpdateCommand menuUpdateCommand = new MenuUpdateCommand();

    menuUpdateCommand.setName(updateRequest.name());
    menuUpdateCommand.setCode(updateRequest.code());
    menuUpdateCommand.setRoleCode(updateRequest.roleCode());
    menuUpdateCommand.setId(updateRequest.id());

    return menuUpdateCommand;
  }

  protected MenuDeleteCommand menuDeleteRequestToMenuDeleteCommand(
      MenuDeleteRequest menuDeleteRequest) {
    if (menuDeleteRequest == null) {
      return null;
    }

    MenuDeleteCommand menuDeleteCommand = new MenuDeleteCommand();

    menuDeleteCommand.setId(menuDeleteRequest.id());

    return menuDeleteCommand;
  }
}
