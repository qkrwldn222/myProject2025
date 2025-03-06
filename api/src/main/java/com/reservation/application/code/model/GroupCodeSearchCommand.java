package com.reservation.application.code.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupCodeSearchCommand {
  private String groupCode;
  private String useYn;
}
