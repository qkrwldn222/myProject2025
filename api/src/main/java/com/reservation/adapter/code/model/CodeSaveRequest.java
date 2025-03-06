package com.reservation.adapter.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

public record CodeSaveRequest(
    @NonNull @Schema(name = "코드", description = "공통 코드") String code,
    @NonNull @Schema(name = "명칭", description = "코드 명") String name,
    @NonNull @Schema(name = "그룹 코드", description = "그룹 코드") String groupCode,
    @Schema(name = "속성1", description = "속성 1") String attr01,
    @Schema(name = "속성2", description = "속성 2") String attr02,
    @Schema(name = "속성3", description = "속성 3") String attr03,
    @Schema(name = "속성4", description = "속성 4") String attr04,
    @Schema(name = "속성5", description = "속성 5") String attr05,
    @Schema(name = "속성6", description = "속성 6") String attr06,
    @Schema(name = "속성7", description = "속성 7") String attr07,
    @Schema(name = "속성8", description = "속성 8") String attr08,
    @Schema(name = "속성9", description = "속성 9") String attr09) {}
