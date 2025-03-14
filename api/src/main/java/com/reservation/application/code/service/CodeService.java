package com.reservation.application.code.service;

import com.reservation.application.code.model.*;
import com.reservation.domain.Code;
import java.util.List;

public interface CodeService {
  List<Code> searchCodes(String groupCode);

  List<Code> findAll();

  void operateCodes(CodeOperateCommand command);

  List<GroupCodeSearchResponse> searchGroupCodes(GroupCodeSearchCommand command);

  void saveGroupCode(GroupCodeSaveCommand command);

  void updateGroupCode(GroupCodeUpdateCommand command);

  void clearMenuCache();
}
