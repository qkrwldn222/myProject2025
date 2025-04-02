package com.reservation.adapter.review.mapper;

import com.reservation.adapter.review.model.ReviewRequest;
import com.reservation.adapter.review.model.ReviewUrlRequest;
import com.reservation.application.review.model.ReviewSaveCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewRequestMapper {
  ReviewRequestMapper INSTANCE = Mappers.getMapper(ReviewRequestMapper.class);

  ReviewSaveCommand toCommand(ReviewRequest request);

  ReviewSaveCommand toCommand(ReviewUrlRequest request);
}
