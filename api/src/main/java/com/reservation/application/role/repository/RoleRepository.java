package com.reservation.application.role.repository;

import com.reservation.common.enums.RoleType;
import com.reservation.domain.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {
  List<Role> findAll();

  Optional<Role> findByRoleType(RoleType roleType);

  Optional<Role> findOneByName(String name);

  void deleteById(int id);

  Role save(Role role);
}
