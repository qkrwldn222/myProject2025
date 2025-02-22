package com.reservation.adapter.store.controller;


import com.reservation.adapter.store.swagger.StoreSwagger;
import com.reservation.common.ApiResponse;
import com.reservation.domain.FoodStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController implements StoreSwagger {

    @Override
    public ResponseEntity<ApiResponse<List<FoodStore>>> getAllFoodStores() {
        return null;
    }
}
