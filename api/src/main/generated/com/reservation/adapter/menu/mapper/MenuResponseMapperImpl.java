package com.reservation.adapter.menu.mapper;

import com.reservation.adapter.menu.model.MenuSearchResponse;
import com.reservation.domain.Menu;
import com.reservation.domain.Role;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class MenuResponseMapperImpl implements MenuResponseMapper {

  @Override
  public MenuSearchResponse toMenuSearchResponse(Menu menu) {
    if (menu == null) {
      return null;
    }

    MenuSearchResponse menuSearchResponse = new MenuSearchResponse();

    menuSearchResponse.setRoleName(menuRoleName(menu));
    menuSearchResponse.setRoleRank(menuRoleRank(menu));
    menuSearchResponse.setId(menu.getId());
    menuSearchResponse.setName(menu.getName());
    menuSearchResponse.setCode(menu.getCode());

    return menuSearchResponse;
  }

  @Override
  public List<MenuSearchResponse> toMenuSearchResponses(List<Menu> menus) {
    if (menus == null) {
      return null;
    }

    List<MenuSearchResponse> list = new ArrayList<MenuSearchResponse>(menus.size());
    for (Menu menu : menus) {
      list.add(toMenuSearchResponse(menu));
    }

    return list;
  }

  private String menuRoleName(Menu menu) {
    if (menu == null) {
      return null;
    }
    Role role = menu.getRole();
    if (role == null) {
      return null;
    }
    String name = role.getName();
    if (name == null) {
      return null;
    }
    return name;
  }

  private int menuRoleRank(Menu menu) {
    if (menu == null) {
      return 0;
    }
    Role role = menu.getRole();
    if (role == null) {
      return 0;
    }
    int rank = role.getRank();
    return rank;
  }
}
