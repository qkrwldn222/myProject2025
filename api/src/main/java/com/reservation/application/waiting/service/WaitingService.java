package com.reservation.application.waiting.service;

import com.reservation.domain.User;
import java.util.List;

public interface WaitingService {

  void leaveWaitingQueue(Long restaurantId);

  void leaveWaitingQueueByOwner(Long restaurantId);

  String getNextUser(Long restaurantId);

  User allowNextUser(Long restaurantId);

  void joinWaitingQueue(Long restaurantId);

  List<String> getWaitingList(Long restaurantId);

  int getUserWaitingPosition(Long restaurantId, String userId);

  void confirmEntry(Long restaurantId);
}
