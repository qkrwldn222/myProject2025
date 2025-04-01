package com.reservation.application.review.mapper;

import com.reservation.application.review.model.ReviewSaveCommand;
import com.reservation.domain.*;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewAppRequestMapper {
  ReviewAppRequestMapper INSTANCE = Mappers.getMapper(ReviewAppRequestMapper.class);

  @Mapping(source = "command.reviewText", target = "reviewText")
  @Mapping(source = "command.userId", target = "userId")
  @Mapping(source = "reviews", target = "reviewImages")
  @Mapping(source = "restaurant", target = "restaurant")
  @Mapping(target = "reservation", expression = "java(toNullable(reservation))") // Optional 처리
  @Mapping(
      target = "waitingHistory",
      expression = "java(toNullable(waitingHistory))") // Optional 처리
  Review toEntity(
      ReviewSaveCommand command,
      Optional<Reservation> reservation,
      Restaurant restaurant,
      Optional<WaitingHistory> waitingHistory,
      List<ReviewImage> reviews);

  // Optional<T> 값을 null 처리하는 기본 메서드
  default <T> T toNullable(Optional<T> optional) {
    return optional.orElse(null);
  }
}
