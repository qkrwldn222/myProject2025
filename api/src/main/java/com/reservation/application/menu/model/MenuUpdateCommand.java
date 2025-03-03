package com.reservation.application.menu.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Data
@NoArgsConstructor
public class MenuUpdateCommand extends MenuSaveCommand implements BaseValidation {

    private Long id;

    @Override
    public void validate() {
        super.validate();
        if(ObjectUtils.isEmpty(id)) throw new ApiException("메뉴 키는 필수 값입니다.");
    }
}
