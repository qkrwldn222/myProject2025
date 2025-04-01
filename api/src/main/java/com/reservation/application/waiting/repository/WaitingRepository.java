package com.reservation.application.waiting.repository;

import com.reservation.domain.WaitingHistory;
import java.util.Optional;

public interface WaitingRepository {
  Optional<WaitingHistory> findById(Long WaitingHistoryId);

  void saveWaitingHistory(WaitingHistory waitingHistory);
}
