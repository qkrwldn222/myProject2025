package com.reservation.infrastructure.menu.repository;

import com.reservation.application.menu.repository.MenuRepository;
import com.reservation.domain.Menu;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuJpaRepositoryAdapter implements MenuRepository {
  private final MenuJpaRepository menuJpaRepository;

  @Override
  public List<Menu> findAll() {
    return menuJpaRepository.findAll();
  }

  @Override
  public Optional<Menu> findById(Long id) {
    return Optional.ofNullable(menuJpaRepository.findMenuById(id));
  }

  @Override
  public void deleteById(Long id) {
    menuJpaRepository.deleteById(id);
  }

  @Override
  public void save(Menu menu) {
    menuJpaRepository.save(menu);
  }
}
