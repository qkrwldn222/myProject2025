package com.reservation.application.menu.model;


import com.reservation.common.valid.BaseValidation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Data
@NoArgsConstructor
public class MenuOperateCommand implements BaseValidation {

    List<MenuSaveCommand> saveList;
    List<MenuUpdateCommand> updateList;
    List<MenuDeleteCommand> deleteList;

    @Override
    public void validate() {
        saveList.stream().forEach(MenuSaveCommand::validate);
        updateList.stream().forEach(MenuUpdateCommand::validate);
        deleteList.stream().forEach(MenuDeleteCommand::validate);
    }
}
