package com.reservation.infrastructure.reservation.specification;

import com.reservation.common.enums.ReservationStatus;
import com.reservation.common.enums.RoleType;
import com.reservation.domain.Reservation;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ReservationSpecification {
  public static Specification<Reservation> search(
      String userId,
      Long restaurantId,
      ReservationStatus reservationStatus,
      LocalDate reservationStartDate,
      LocalDate reservationEndDate,
      String currentUserId,
      RoleType currentUserRole,
      Long currentOwnerRestaurantId) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // 관리자(admin)는 모든 데이터 접근 가능
      if (RoleType.ADMIN.equals(currentUserRole)) {
        if (userId != null) {
          predicates.add(criteriaBuilder.equal(root.get("user").get("userId"), userId));
        }
        if (restaurantId != null) {
          predicates.add(criteriaBuilder.equal(root.get("restaurant").get("id"), restaurantId));
        }
      }

      // 가게 운영자는 자기 가게만 조회 가능
      else if (RoleType.STORE_OWNER.equals(currentUserRole)) {
        predicates.add(
            criteriaBuilder.equal(root.get("restaurant").get("id"), currentOwnerRestaurantId));
        if (userId != null) {
          predicates.add(criteriaBuilder.equal(root.get("user").get("userId"), userId));
        }
      }

      // 일반 유저는 자신만 조회 가능
      else {
        predicates.add(criteriaBuilder.equal(root.get("user").get("userId"), currentUserId));
      }

      // 예약 상태 조건 추가
      if (reservationStatus != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), reservationStatus));
      }
      // 날짜 조건 추가
      if (reservationStartDate != null) {
        predicates.add(
            criteriaBuilder.greaterThanOrEqualTo(
                root.get("reservationDate"), reservationStartDate));
      }
      if (reservationEndDate != null) {
        predicates.add(
            criteriaBuilder.lessThanOrEqualTo(root.get("reservationDate"), reservationEndDate));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
