package com.reservation.infrastructure.role.repository;

import com.reservation.common.config.RoleType;
import com.reservation.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Integer> {
    List<Role> findAll();

    Optional<Role> findByRoleType(RoleType roleType);

    Optional<Role> findOneByName(String name);

    void deleteById(int id);
}
