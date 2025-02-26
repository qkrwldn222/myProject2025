package com.reservation.infrastructure.menu;


import com.reservation.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByRoleId(Long roleId);

    List<Menu> findAllByRoleId(Long roleId, Pageable pageable);

}
