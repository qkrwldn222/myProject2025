package com.reservation.application.user.model;


import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class SignupCommand implements BaseValidation {
    private String name;
    private String username;
    private String password;
    private Integer roleId;

    @Override
    public void validate() {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Username은 필수 값입니다.");
        }
        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Password는 필수 값입니다.");
        }
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Name은 필수 값입니다.");
        }
    }
}
