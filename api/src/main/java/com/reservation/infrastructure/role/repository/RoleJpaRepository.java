package com.reservation.infrastructure.role.repository;

import com.reservation.common.enums.RoleType;
import com.reservation.domain.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByRoleType(RoleType roleType);

  Optional<Role> findOneByName(String name);
}
