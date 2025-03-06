package com.reservation.application.code.repository;

import com.reservation.application.code.model.GroupCodeSaveCommand;
import com.reservation.application.code.model.GroupCodeSearchCommand;
import com.reservation.application.code.model.GroupCodeUpdateCommand;
import com.reservation.infrastructure.code.model.GroupCodeDTO;
import java.util.List;

public interface CodeRepository {

  List<GroupCodeDTO> searchGroupCodes(GroupCodeSearchCommand command);

  void saveGroupCode(GroupCodeSaveCommand command);

  void updateGroupCode(GroupCodeUpdateCommand command);
}
