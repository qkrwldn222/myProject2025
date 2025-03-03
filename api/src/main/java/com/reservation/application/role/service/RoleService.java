package com.reservation.application.role.service;

import com.reservation.application.role.model.RoleSearchCommand;
import com.reservation.domain.Role;
import java.util.List;

public interface RoleService {
  List<Role> searchAllByCondition(RoleSearchCommand command);

  List<Role> findAll();

  Role findOneByCode(String code);

  Role findOneByName(String name);
}
