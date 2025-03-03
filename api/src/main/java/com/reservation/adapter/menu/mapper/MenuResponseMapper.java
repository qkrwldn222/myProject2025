package com.reservation.adapter.menu.mapper;

import com.reservation.adapter.menu.model.MenuSearchResponse;
import com.reservation.domain.Menu;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuResponseMapper {

  MenuResponseMapper INSTANCE = Mappers.getMapper(MenuResponseMapper.class);

  @Mapping(source = "role.name", target = "roleName")
  @Mapping(source = "role.rank", target = "roleRank")
  MenuSearchResponse toMenuSearchResponse(Menu menu);

  List<MenuSearchResponse> toMenuSearchResponses(List<Menu> menus);
}
