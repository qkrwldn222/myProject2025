package com.reservation.adapter.review.swagger;

import com.reservation.adapter.review.model.ReviewRequest;
import com.reservation.adapter.review.model.ReviewUrlRequest;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "리뷰", description = "리뷰 관련 API를 호출합니다.")
@RequestMapping("reviews")
public interface ReviewSwagger {
  @Operation(
      summary = "리뷰 작성",
      description = "리뷰 텍스트와 이미지 파일을 함께 업로드합니다.",
      responses = {
        @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping(path = "/operate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseEntity<ApiResponseWrapper<Void>> createReview(
      @RequestPart("review") ReviewRequest reviewRequest,
      @RequestPart(name = "images", required = false) List<MultipartFile> images);

  @Operation(
      summary = "파일 업로드",
      description = "이미지 또는 기타 파일을 업로드 합니다.",
      responses = {
        @ApiResponse(responseCode = "200", description = "업로드 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<ApiListResponse<String>> uploadFiles(
      @RequestPart(name = "files") List<MultipartFile> files);

  @Operation(
      summary = "리뷰 작성",
      description = "리뷰 텍스트와 이미지 파일을 함께 업로드합니다.",
      responses = {
        @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
      })
  @PostMapping(path = "/operateUrl")
  ResponseEntity<ApiResponseWrapper<Void>> createReview(
      @RequestBody ReviewUrlRequest reviewRequest);
}
