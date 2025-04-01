package com.reservation.infrastructure.waiting.repository;

import com.reservation.application.waiting.repository.WaitingRepository;
import com.reservation.domain.WaitingHistory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingHistoryJpaRepositoryAdapter implements WaitingRepository {

  private final WaitingHistoryJpaRepository waitingHistoryJpaRepository;

  @Override
  public Optional<WaitingHistory> findById(Long WaitingHistoryId) {
    return waitingHistoryJpaRepository.findById(WaitingHistoryId);
  }

  @Override
  public void saveWaitingHistory(WaitingHistory waitingHistory) {
    waitingHistoryJpaRepository.save(waitingHistory);
  }
}
