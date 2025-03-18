package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.application.restaurant.restaurant.repository.RestaurantMenuJpaRepository;
import com.reservation.domain.RestaurantMenu;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantMenuJpaRepositoryAdapter implements RestaurantMenuJpaRepository {

  private final RestaurantMenuSpringDataJpaRepository restaurantMenuJpaRepository;

  @Override
  public List<RestaurantMenu> findAllByRestaurantId(Long restaurantId) {
    return restaurantMenuJpaRepository.findAllByRestaurantId(restaurantId);
  }

  @Override
  public void saveAll(List<RestaurantMenu> menus) {
    restaurantMenuJpaRepository.saveAll(menus);
  }

  @Override
  public Optional<RestaurantMenu> findById(Long id) {
    return restaurantMenuJpaRepository.findById(id);
  }

  @Override
  public void deleteById(Long id) {
    restaurantMenuJpaRepository.deleteById(id);
  }

  @Override
  public void deleteAllById(List<Long> ids) {
    restaurantMenuJpaRepository.deleteAllById(ids);
  }
}
