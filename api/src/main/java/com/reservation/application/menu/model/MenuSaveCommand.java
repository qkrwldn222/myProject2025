package com.reservation.application.menu.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class MenuSaveCommand implements BaseValidation {

    private String name;
    private String code;
    private String roleCode;

    @Override
    public void validate() {
        if (!StringUtils.hasText(name) || !StringUtils.hasText(code)){
            throw new ApiException("메뉴 명과 코드는 필수 값 입니다.");
        }
    }
}
