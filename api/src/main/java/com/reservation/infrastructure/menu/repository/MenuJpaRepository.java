package com.reservation.infrastructure.menu.repository;


import com.reservation.domain.Menu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuJpaRepository extends JpaRepository<Menu, Long> {

    @EntityGraph(attributePaths = {"role"})
    List<Menu> findAll();

    Menu findMenuById(Long id);
}
