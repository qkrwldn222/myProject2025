package com.reservation.adapter.store.swagger;

import com.reservation.adapter.store.model.FoodStoreSearchResponse;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "음식점", description = "음식점 관련 API")
@RequestMapping("/food-store")
public interface FoodStoreSwagger {

  @Operation(summary = "음식점 목록 조회", description = "음식점 데이터를 조회합니다.")
  @SecurityRequirement(name = "BearerAuth")
  @GetMapping
  ResponseEntity<ApiListResponse<FoodStoreSearchResponse>> getAllFoodStores(
      @RequestParam(value = "mgtNo", required = false) String mgtNo,
      @RequestParam(value = "bplcNm", required = false) String bplcNm,
      @RequestParam(value = "rdnWhlAddr", required = false) String rdnWhlAddr,
      @RequestParam(value = "trdStateGbn", defaultValue = "01", name = "영업 상태코드")
          String trdStateGbn);

  @Operation(
      summary = "음식점 목록 동기화",
      description = "음식점 데이터를 동기화하여 저장합니다.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "동기화 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("sync")
  ResponseEntity<ApiResponseWrapper<Void>> syncFoodStores();
}
