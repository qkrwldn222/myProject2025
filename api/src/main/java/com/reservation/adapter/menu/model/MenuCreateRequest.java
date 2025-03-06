package com.reservation.adapter.menu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record MenuCreateRequest(
    @NonNull @Schema(name = "name", description = "메뉴 명") String name,
    @NonNull @Schema(name = "code", description = "메뉴 코드") String code,
    @NonNull @Schema(name = "roleCode", description = "권한코드") String roleCode) {}
