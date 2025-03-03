package com.reservation.adapter.menu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record MenuDeleteRequest(@NonNull @Schema(name = "id", description = "메뉴 키") Long id) {}
