package com.reservation.application.waiting.service;

import com.reservation.domain.User;
import com.reservation.domain.WaitingHistory;
import java.util.List;
import java.util.Optional;

public interface WaitingService {

  void leaveWaitingQueue(Long restaurantId);

  void leaveWaitingQueueByOwner(Long restaurantId);

  String getNextUser(Long restaurantId);

  User allowNextUser(Long restaurantId);

  void joinWaitingQueue(Long restaurantId);

  List<String> getWaitingList(Long restaurantId);

  int getUserWaitingPosition(Long restaurantId, String userId);

  void confirmEntry(Long restaurantId);

  Optional<WaitingHistory> findById(Long waitingHistoryId);
}
