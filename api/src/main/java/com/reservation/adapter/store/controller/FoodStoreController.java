package com.reservation.adapter.store.controller;


import com.reservation.adapter.store.swagger.StoreSwagger;
import com.reservation.application.store.service.StoreService;
import com.reservation.common.ApiResponse;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.domain.FoodStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
@RequestMapping("/store")
public class FoodStoreController implements StoreSwagger {

    private final StoreService storeService;

    @Override
    @GetMapping("/all-food")
    public ResponseEntity<ApiResponse<List<FoodStore>>> getAllFoodStores() {
        return null;
    }

    @Override
    @PostMapping("sync-food")
    public ResponseEntity<?> syncFoodStores() {
        storeService.foodBulkInsert();
        return ResponseEntity.ok("User sync successfully!");
    }
}
