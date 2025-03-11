package com.reservation.infrastructure.refund.repository;

import com.reservation.domain.RefundPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefundJpaRepository extends JpaRepository<RefundPolicy,Long> {

    Optional<RefundPolicy> findById(Long id);

    List<RefundPolicy> findAllByRestaurantId(Long id);
}
