package com.reservation.adapter.menu.controller;

import com.reservation.adapter.menu.mapper.MenuRequestMapper;
import com.reservation.adapter.menu.mapper.MenuResponseMapper;
import com.reservation.adapter.menu.model.MenuOperateRequest;
import com.reservation.adapter.menu.model.MenuSearchResponse;
import com.reservation.adapter.menu.swagger.MenuSwagger;
import com.reservation.application.menu.model.MenuOperateCommand;
import com.reservation.application.menu.model.MenuSearchCommand;
import com.reservation.application.menu.service.MenuService;
import com.reservation.common.ApiResponse;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.domain.Menu;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
public class MenuController implements MenuSwagger {

    private final MenuService menuService;

    @GetMapping("/search")
    @Override
    public ResponseEntity<ApiResponse<List<MenuSearchResponse>>> search(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "roleName", required = false) String roleName) {

        MenuSearchCommand command = MenuRequestMapper.INSTANCE.toSearchCommand(name, code, roleName);

        List<Menu> menus = menuService.searchMenu(command);

        List<MenuSearchResponse> result = MenuResponseMapper.INSTANCE.toMenuSearchResponses(menus);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Override
    @PostMapping("/operate")
    public ResponseEntity<ApiResponse<?>> operateMenu(MenuOperateRequest request) {
        MenuOperateCommand operateCommand = MenuRequestMapper.INSTANCE.toOperateCommand(request);

        menuService.operateMenu(operateCommand);

        return ResponseEntity.ok(ApiResponse.success("요청이 완료되었습니다."));
    }
}
