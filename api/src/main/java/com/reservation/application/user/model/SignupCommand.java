package com.reservation.application.user.model;


import com.reservation.common.valid.BaseValidation;
import com.reservation.common.config.ApiException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class SignupCommand implements BaseValidation {
    private String userID;
    private String userName;
    private String password;
    private String roleCode;

    @Override
    public void validate() {
        if (!StringUtils.hasText(userID)) {
            throw new ApiException("userID 필수 값입니다.");
        }
        if (!StringUtils.hasText(password)) {
            throw new ApiException("Password는 필수 값입니다.");
        }
        if (!StringUtils.hasText(userName)) {
            throw new ApiException("userName 필수 값입니다.");
        }
        if (!StringUtils.hasText(roleCode)) {
            throw new ApiException("roleCode 필수 값입니다.");
        }
    }
}
