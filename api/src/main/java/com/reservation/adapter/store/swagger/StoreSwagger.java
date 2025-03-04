package com.reservation.adapter.store.swagger;


import com.reservation.common.ApiResponse;
import com.reservation.domain.FoodStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "음식점", description = "음식점 관련 API")
@RequestMapping("/store")
public interface StoreSwagger {

    @Operation(summary = "음식점 목록 조회", description = "음식점 데이터를 조회합니다.")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    ResponseEntity<ApiResponse<List<FoodStore>>> getAllFoodStores();


    @Operation(summary = "음식점 목록 동기화", description = "음식점 데이터를 동기화합니다.")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("sync")
    ResponseEntity<?> syncFoodStores();

}
