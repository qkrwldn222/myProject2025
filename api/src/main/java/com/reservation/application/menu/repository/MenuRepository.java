package com.reservation.application.menu.repository;

import com.reservation.domain.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuRepository {
  List<Menu> findAll();

  Optional<Menu> findById(Long id);

  void deleteById(Long id);

  void save(Menu menu);
}
