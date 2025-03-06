package com.reservation.application.menu.service;

import com.reservation.application.menu.model.MenuOperateCommand;
import com.reservation.application.menu.model.MenuSearchCommand;
import com.reservation.domain.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuService {
  List<Menu> searchMenu(MenuSearchCommand command);

  Optional<Menu> findMenuByCode(String menuCode);

  void operateMenu(MenuOperateCommand command);

  List<Menu> findAll();
}
