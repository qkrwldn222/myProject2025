package com.reservation.adapter.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record CodeUpdateRequest(
    @NonNull @Schema(name = "id", description = "기본 키") Long id,
    @NonNull @Schema(name = "name", description = "코드 명") String name,
    @Schema(name = "속성1", description = "속성 1") String attr01,
    @Schema(name = "속성2", description = "속성 2") String attr02,
    @Schema(name = "속성3", description = "속성 3") String attr03,
    @Schema(name = "속성4", description = "속성 4") String attr04,
    @Schema(name = "속성5", description = "속성 5") String attr05,
    @Schema(name = "속성6", description = "속성 6") String attr06,
    @Schema(name = "속성7", description = "속성 7") String attr07,
    @Schema(name = "속성8", description = "속성 8") String attr08,
    @Schema(name = "속성9", description = "속성 9") String attr09) {}
