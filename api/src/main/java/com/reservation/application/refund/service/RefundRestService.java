package com.reservation.application.refund.service;

import com.reservation.domain.RefundPolicy;
import com.reservation.infrastructure.refund.repository.RefundJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundRestService implements RefundService {

  private final RefundJpaRepository refundJpaRepository;

  @Override
  public List<RefundPolicy> findByRestaurantId(Long id) {
    return refundJpaRepository.findAllByRestaurantId(id);
  }
}
