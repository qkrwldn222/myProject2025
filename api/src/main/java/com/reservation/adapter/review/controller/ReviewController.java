package com.reservation.adapter.review.controller;

import com.reservation.adapter.review.mapper.ReviewRequestMapper;
import com.reservation.adapter.review.model.ReviewRequest;
import com.reservation.adapter.review.model.ReviewUrlRequest;
import com.reservation.adapter.review.swagger.ReviewSwagger;
import com.reservation.application.review.model.ReviewSaveCommand;
import com.reservation.application.review.service.ReviewService;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
public class ReviewController implements ReviewSwagger {

  private final ReviewService reviewService;

  @PostMapping(path = "/operate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<ApiResponseWrapper<Void>> createReview(
      @RequestPart("review") ReviewRequest reviewRequest,
      @RequestPart(name = "images", required = false) List<MultipartFile> images) {
    ReviewSaveCommand command = ReviewRequestMapper.INSTANCE.toCommand(reviewRequest);

    reviewService.saveReview(command, images);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("리뷰가 등록되었습니다."));
  }

  @Override
  @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiListResponse<String>> uploadFiles(
      @RequestPart(name = "files") List<MultipartFile> files) {

    List<String> strings = reviewService.saveImage(files);

    return ResponseEntity.ok(ApiListResponse.multiResult("SUCCESS", strings));
  }

  @Override
  @PostMapping(path = "/operateUrl")
  public ResponseEntity<ApiResponseWrapper<Void>> createReview(
      @RequestBody ReviewUrlRequest reviewRequest) {

    ReviewSaveCommand command = ReviewRequestMapper.INSTANCE.toCommand(reviewRequest);

    reviewService.saveReview(command);

    return ResponseEntity.ok(ApiResponseWrapper.actionResult("리뷰가 등록되었습니다."));
  }

  //    @Override
  //    @PostMapping(path = "/operate", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  //    public ResponseEntity<ApiResponseWrapper<Void>> createReview(@RequestPart(name = "review")
  // ReviewRequest reviewRequest,
  //                                                                 @RequestPart(name = "images",
  // required = false) List<MultipartFile> images) {
  ////        ReviewSaveCommand command = ReviewRequestMapper.INSTANCE.toCommand(reviewRequest);
  //
  ////        reviewService.saveReview(command , images);
  //
  //        return ResponseEntity.ok(ApiResponseWrapper.actionResult("리뷰가 등록되었습니다."));
  //    }
}
