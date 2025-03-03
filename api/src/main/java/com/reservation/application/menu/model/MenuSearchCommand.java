package com.reservation.application.menu.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MenuSearchCommand {
    private String name;
    private String code;
    private String roleName;
}
