package com.reservation.application.role.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleSearchCommand {
  private String name;
  private String code;
  private int rank;
}
