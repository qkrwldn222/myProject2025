package com.reservation.application.code.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class GroupCodeSearchResponse {
  private String groupCode;
  private String useYn;
  private String description;
  private Long id;
}
