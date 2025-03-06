package com.reservation.adapter.menu.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuSearchResponse {
  @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "key", description = "1")
  private Long id;

  @Schema(name = "메뉴 명", description = "가게")
  private String name;

  @Schema(accessMode = Schema.AccessMode.READ_ONLY, name = "코드", description = "menu")
  private String code;

  @Schema(name = "권한명", description = "게스트")
  private String roleName;

  @Schema(name = "권한 순위", description = "1")
  private int roleRank;
}
