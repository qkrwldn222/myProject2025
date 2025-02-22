package com.reservation.adapter.store.swagger;


import com.reservation.common.ApiResponse;
import com.reservation.domain.FoodStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "음식점", description = "음식점 관련 API")
@RequestMapping("/api/store")
public interface StoreSwagger {

    @Operation(summary = "음식점 목록 조회", description = "음식점 데이터를 조회합니다.")
    @GetMapping
    ResponseEntity<ApiResponse<List<FoodStore>>> getAllFoodStores();
}
