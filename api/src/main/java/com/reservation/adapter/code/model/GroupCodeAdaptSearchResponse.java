package com.reservation.adapter.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupCodeAdaptSearchResponse {
  @Schema(name = "groupCode", description = "그룹코드", example = "FOOD_STORE_STATE")
  private String groupCode;

  @Schema(name = "useYn", description = "사용여부", example = "Y")
  private String useYn;

  @Schema(name = "description", description = "설명", example = "메뉴 영업상태 코드")
  private String description;

  @Schema(name = "id", description = "기본키", example = "1")
  private Long id;
}
