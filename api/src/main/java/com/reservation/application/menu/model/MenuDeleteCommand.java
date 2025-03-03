package com.reservation.application.menu.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Data
@NoArgsConstructor
public class MenuDeleteCommand implements BaseValidation {

    private Long id;

    @Override
    public void validate() {
        if(ObjectUtils.isEmpty(id)) {
            throw new ApiException("id는 필수 값입니다.");
        }
    }
}
