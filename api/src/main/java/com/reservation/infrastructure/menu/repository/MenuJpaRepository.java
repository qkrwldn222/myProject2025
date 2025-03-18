package com.reservation.infrastructure.menu.repository;

import com.reservation.domain.Menu;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

  @EntityGraph(attributePaths = {"role"})
  List<Menu> findAll();

  Menu findMenuById(Long id);
}
