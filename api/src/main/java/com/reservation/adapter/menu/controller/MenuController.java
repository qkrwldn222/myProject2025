package com.reservation.adapter.menu.controller;

import com.reservation.adapter.menu.mapper.MenuRequestMapper;
import com.reservation.adapter.menu.mapper.MenuResponseMapper;
import com.reservation.adapter.menu.model.MenuOperateRequest;
import com.reservation.adapter.menu.model.MenuSearchResponse;
import com.reservation.adapter.menu.swagger.MenuSwagger;
import com.reservation.application.menu.model.MenuOperateCommand;
import com.reservation.application.menu.model.MenuSearchCommand;
import com.reservation.application.menu.service.MenuService;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import com.reservation.domain.Menu;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
public class MenuController implements MenuSwagger {

  private final MenuService menuService;

  @GetMapping("/search")
  @Override
  public ResponseEntity<ApiListResponse<MenuSearchResponse>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "roleName", required = false) String roleName) {

    MenuSearchCommand command = MenuRequestMapper.INSTANCE.toSearchCommand(name, code, roleName);

    List<Menu> menus = menuService.searchMenu(command);

    List<MenuSearchResponse> result = MenuResponseMapper.INSTANCE.toMenuSearchResponses(menus);

    return ResponseEntity.ok(ApiListResponse.multiResult("메뉴 조회 성공", result));
  }

  @Override
  @PostMapping("/operate")
  public ResponseEntity<ApiResponseWrapper<Void>> operateMenu(MenuOperateRequest request) {
    MenuOperateCommand operateCommand = MenuRequestMapper.INSTANCE.toOperateCommand(request);

    menuService.operateMenu(operateCommand);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("메뉴 저장이 완료되었습니다."));
  }
}
