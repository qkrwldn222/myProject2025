package com.reservation.adapter.menu.swagger;


import com.reservation.adapter.menu.model.MenuOperateRequest;
import com.reservation.adapter.menu.model.MenuSearchResponse;
import com.reservation.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "메뉴", description = "메뉴 관련 API")
@RequestMapping("/menu")
@SecurityRequirement(name = "BearerAuth")
public interface MenuSwagger {

    @Operation(summary = "메뉴 목록 조회", description = "메뉴 데이터를 검색합니다.")
    @GetMapping("/search")
    ResponseEntity<ApiResponse<List<MenuSearchResponse>>> search(@RequestParam(value = "name", required = false) String name,
                                                                 @RequestParam(value = "code", required = false) String code,
                                                                 @RequestParam(value = "roleName", required = false) String roleName);


    @Operation(summary = "메뉴 CRUD 요청", description = "메뉴 데이터 저장, 삭제, 수정 요청을 호출합니다.")
    @PostMapping("/operate")
    ResponseEntity<ApiResponse<?>> operateMenu(@RequestBody MenuOperateRequest command);


}
