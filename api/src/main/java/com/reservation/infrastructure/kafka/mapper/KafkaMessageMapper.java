package com.reservation.infrastructure.kafka.mapper;

import com.reservation.domain.Review;
import com.reservation.infrastructure.kafka.model.ReviewMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KafkaMessageMapper {
  KafkaMessageMapper INSTANCE = Mappers.getMapper(KafkaMessageMapper.class);

  ReviewMessage toReviewMessage(Review review);
}
