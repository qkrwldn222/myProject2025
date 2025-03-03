package com.reservation.adapter.menu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record MenuUpdateRequest(
    @NonNull @Schema(name = "id", description = "메뉴 키") Long id,
    @Schema(name = "name", description = "메뉴 명") String name,
    @Schema(name = "code", description = "메뉴 코드") String code,
    @Schema(name = "roleCode", description = "메뉴 권한코드") String roleCode) {}
