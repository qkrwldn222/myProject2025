package com.reservation.infrastructure.waiting.repository;

import com.reservation.domain.WaitingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingHistoryJpaRepository extends JpaRepository<WaitingHistory, Long> {}
