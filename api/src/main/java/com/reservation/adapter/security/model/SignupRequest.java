package com.reservation.adapter.security.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class SignupRequest {

    private String name;
    private String username;
    private String password;
    private Integer roleId;
}
