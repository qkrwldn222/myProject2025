package com.reservation.infrastructure.restaurant.restaurant.repository;

import com.reservation.application.restaurant.restaurant.repository.RestaurantSeatJpaRepository;
import com.reservation.domain.RestaurantSeat;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RestaurantSeatJpaAdapter implements RestaurantSeatJpaRepository {

  private final RestaurantSeatSpringDataJpaRepository restaurantSeatSpringDataJpaRepository;

  @Override
  public Optional<RestaurantSeat> findById(Long id) {
    return restaurantSeatSpringDataJpaRepository.findBySeatId(id);
  }

  @Override
  public Optional<RestaurantSeat> findBySeatIdAndRestaurantId(Long id, Long restaurantId) {
    return restaurantSeatSpringDataJpaRepository.findBySeatIdAndRestaurantId(id, restaurantId);
  }

  @Override
  public List<RestaurantSeat> findAllByRestaurantId(Long restaurantId) {
    return restaurantSeatSpringDataJpaRepository.findAllByRestaurantId(restaurantId);
  }

  @Override
  public void saveAll(List<RestaurantSeat> restaurantSeats) {
    restaurantSeatSpringDataJpaRepository.saveAll(restaurantSeats);
  }

  @Override
  public void deleteAllById(List<Long> ids) {
    restaurantSeatSpringDataJpaRepository.deleteAllById(ids);
  }
}
