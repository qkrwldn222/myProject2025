package com.reservation.application.refund.service;

import com.reservation.domain.RefundPolicy;
import com.reservation.infrastructure.refund.repository.RefundJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundRestService implements RefundService{

    private final RefundJpaRepository refundJpaRepository;

    @Override
    public List<RefundPolicy> findByRestaurantId(Long id) {
        return refundJpaRepository.findAllByRestaurantId(id);
    }
}
