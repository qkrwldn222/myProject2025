package com.reservation.infrastructure.role.repository;

import com.reservation.application.role.repository.RoleRepository;
import com.reservation.common.enums.RoleType;
import com.reservation.domain.Role;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RoleJpaRepositoryAdapter implements RoleRepository {

  private final RoleJpaRepository roleJpaRepository;

  public RoleJpaRepositoryAdapter(RoleJpaRepository roleJpaRepository) {
    this.roleJpaRepository = roleJpaRepository;
  }

  @Override
  public List<Role> findAll() {
    return roleJpaRepository.findAll();
  }

  @Override
  public Optional<Role> findByRoleType(RoleType roleType) {
    return roleJpaRepository.findByRoleType(roleType);
  }

  @Override
  public Optional<Role> findOneByName(String name) {
    return roleJpaRepository.findOneByName(name);
  }

  @Override
  public void deleteById(int id) {
    roleJpaRepository.deleteById(id);
  }

  @Override
  public Role save(Role role) {
    return roleJpaRepository.save(role);
  }
}
