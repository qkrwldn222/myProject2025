package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.application.restaurant.restaurant.repository.RestaurantOperatingJpaRepository;
import com.reservation.domain.RestaurantOperatingHours;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantOperatingHoursJpaRepositoryAdapter
    implements RestaurantOperatingJpaRepository {

  private final RestaurantOperatingHoursJpaRepository restaurantOperatingHoursJpaRepository;

  @Override
  public List<RestaurantOperatingHours> findAllByRestaurantId(Long restaurantId) {
    return restaurantOperatingHoursJpaRepository.findAllByRestaurantId(restaurantId);
  }

  @Override
  public void saveAll(List<RestaurantOperatingHours> restaurantOperatingHours) {
    restaurantOperatingHoursJpaRepository.saveAll(restaurantOperatingHours);
  }

  @Override
  public Optional<RestaurantOperatingHours> findById(Long id) {
    return restaurantOperatingHoursJpaRepository.findById(id);
  }

  @Override
  public void deleteAllById(List<Long> ids) {
    restaurantOperatingHoursJpaRepository.deleteAllById(ids);
  }
}
