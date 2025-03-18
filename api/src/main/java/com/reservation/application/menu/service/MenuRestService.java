package com.reservation.application.menu.service;

import com.reservation.application.menu.model.MenuOperateCommand;
import com.reservation.application.menu.model.MenuSaveCommand;
import com.reservation.application.menu.model.MenuSearchCommand;
import com.reservation.application.menu.model.MenuUpdateCommand;
import com.reservation.application.role.service.RoleService;
import com.reservation.common.config.ApiException;
import com.reservation.domain.Menu;
import com.reservation.domain.Role;
import com.reservation.infrastructure.menu.repository.MenuJpaRepositoryAdapter;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class MenuRestService implements MenuService {

  private static final Logger logger = LoggerFactory.getLogger(MenuRestService.class);

  private final MenuJpaRepositoryAdapter menuJpaRepository;
  private final RoleService roleService;

  public List<Menu> findAllMenus() {
    MenuService proxyInstance =
        (MenuService)
            Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {MenuService.class},
                (proxy, method, args) -> method.invoke(this, args));
    return proxyInstance.findAll();
  }

  @Cacheable(value = "menus")
  @Override
  public List<Menu> findAll() {
    return menuJpaRepository.findAll();
  }

  // 캐시 초기화 (무효화)
  @CacheEvict(value = "menus", allEntries = true)
  public void clearMenuCache() {
    logger.info("메뉴 정보 캐시를 초기화했습니다.");
  }

  @Override
  public List<Menu> searchMenu(MenuSearchCommand command) {
    List<Menu> menus = findAllMenus();
    if (ObjectUtils.isEmpty(command)) return menus;

    // 이름 검색
    if (StringUtils.hasText(command.getName()))
      menus.removeIf(menu -> !menu.getName().contains(command.getName()));
    // 코드 검색
    if (StringUtils.hasText(command.getCode()))
      menus.removeIf(menu -> !menu.getCode().equals(command.getCode()));
    // 권한 명 검색
    if (StringUtils.hasText(command.getRoleName()))
      menus.removeIf(menu -> !menu.getRole().getName().equals(command.getRoleName()));

    return menus;
  }

  @Override
  @Transactional
  public void operateMenu(MenuOperateCommand command) {
    command.validate();
    // CREATE 처리
    if (!CollectionUtils.isEmpty(command.getSaveList())) {
      command.getSaveList().forEach(this::saveMenu);
    }

    // UPDATE 처리
    if (!CollectionUtils.isEmpty(command.getUpdateList())) {
      command.getUpdateList().forEach(this::updateMenu);
    }

    // DELETE 처리
    if (!CollectionUtils.isEmpty(command.getDeleteList())) {
      command.getDeleteList().forEach(delete -> this.deleteMenu(delete.getId()));
    }
  }

  public void saveMenu(MenuSaveCommand command) {
    Role role = roleService.findOneByCode(command.getRoleCode());

    Menu menu = Menu.builder().code(command.getCode()).name(command.getName()).role(role).build();

    menuJpaRepository.save(menu);
  }

  public void updateMenu(MenuUpdateCommand command) {
    Menu menu =
        menuJpaRepository
            .findById(command.getId())
            .orElseThrow(() -> new ApiException("메뉴를 찾을 수 없습니다."));
    if (ObjectUtils.isEmpty(menu)) {
      logger.info("메뉴를 찾을 수 없습니다. :" + command.getId());
      throw new ApiException("메뉴를 찾을 수 없습니다. :" + command.getId());
    }
    if (StringUtils.hasText(command.getName())) {
      menu.setName(command.getName());
    }
    if (StringUtils.hasText(command.getCode())) {
      menu.setCode(command.getCode());
    }
    if (StringUtils.hasText(command.getRoleCode())) {
      Role role = roleService.findOneByCode(command.getRoleCode());
      menu.setRole(role);
    }
    menuJpaRepository.save(menu);
  }

  public void deleteMenu(Long menuId) {
    menuJpaRepository.deleteById(menuId);
  }

  @Override
  public Optional<Menu> findMenuByCode(String menuCode) {
    for (Menu menu : findAllMenus()) {
      System.out.println(menu.getCode());
    }
    return findAllMenus().stream().filter(menu -> menu.getCode().equals(menuCode)).findFirst();
  }
}
