package com.reservation.application.refund.service;

import com.reservation.domain.RefundPolicy;
import java.util.List;

public interface RefundService {
  List<RefundPolicy> findByRestaurantId(Long id);
}
