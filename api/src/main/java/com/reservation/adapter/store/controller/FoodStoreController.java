package com.reservation.adapter.store.controller;

import com.reservation.adapter.store.mapper.FoodStoreRequestMapper;
import com.reservation.adapter.store.mapper.FoodStoreResponseMapper;
import com.reservation.adapter.store.model.FoodStoreSearchResponse;
import com.reservation.adapter.store.swagger.FoodStoreSwagger;
import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.application.store.service.FoodStoreService;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import com.reservation.domain.FoodStore;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
@RequestMapping("/food-store")
public class FoodStoreController implements FoodStoreSwagger {

  private final FoodStoreService foodStoreService;

  @Override
  @GetMapping("/all-food")
  public ResponseEntity<ApiListResponse<FoodStoreSearchResponse>> getAllFoodStores(
      @RequestParam(value = "mgtNo", required = false) String mgtNo,
      @RequestParam(value = "bplcNm", required = false) String bplcNm,
      @RequestParam(value = "rdnWhlAddr", required = false) String rdnWhlAddr,
      @RequestParam(value = "trdStateGbn", required = true, defaultValue = "01")
          String trdStateGbn) {
    FoodStoreSearchCommand storeSearchCommand =
        FoodStoreRequestMapper.INSTANCE.toStoreSearchCommand(
            mgtNo, bplcNm, rdnWhlAddr, trdStateGbn);

    List<FoodStore> foodStores = foodStoreService.searchFoodStores(storeSearchCommand);

    List<FoodStoreSearchResponse> result =
        FoodStoreResponseMapper.INSTANCE.toFoodStoreSearchResponses(foodStores);

    return ResponseEntity.ok(ApiListResponse.multiResult("SUCCESS", result));
  }

  @Override
  @PostMapping("sync-food")
  public ResponseEntity<ApiResponseWrapper<Void>> syncFoodStores() {
    foodStoreService.foodBulkInsert();
    return ResponseEntity.ok(ApiResponseWrapper.actionResult("동기화가 완료되었습니다."));
  }
}
