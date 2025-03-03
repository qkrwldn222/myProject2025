package com.reservation.adapter.store.controller;

import com.reservation.adapter.store.mapper.FoodStoreRequestMapper;
import com.reservation.adapter.store.mapper.FoodStoreResponseMapper;
import com.reservation.adapter.store.model.FoodStoreSearchResponse;
import com.reservation.adapter.store.swagger.FoodStoreSwagger;
import com.reservation.application.store.model.FoodStoreSearchCommand;
import com.reservation.application.store.service.FoodStoreService;
import com.reservation.common.ApiResponse;
import com.reservation.common.config.EnableGlobalExceptionHandling;
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
  public ResponseEntity<ApiResponse<List<FoodStoreSearchResponse>>> getAllFoodStores(
      @RequestParam(value = "mgtNo", required = false) String mgtNo,
      @RequestParam(value = "bplcNm", required = false) String bplcNm,
      @RequestParam(value = "rdnWhlAddr", required = false) String rdnWhlAddr) {
    FoodStoreSearchCommand storeSearchCommand =
        FoodStoreRequestMapper.INSTANCE.toStoreSearchCommand(mgtNo, bplcNm, rdnWhlAddr);

    List<FoodStore> foodStores = foodStoreService.searchFoodStores(storeSearchCommand);

    List<FoodStoreSearchResponse> result =
        FoodStoreResponseMapper.INSTANCE.toFoodStoreSearchResponses(foodStores);

    return ResponseEntity.ok(ApiResponse.success(result));
  }

  @Override
  @PostMapping("sync-food")
  public ResponseEntity<?> syncFoodStores() {
    foodStoreService.foodBulkInsert();
    return ResponseEntity.ok("foodStore sync successfully!");
  }
}
