package com.reservation.adapter.restaurant.controller;

import com.reservation.adapter.restaurant.swagger.RestauranImageSwagger;
import com.reservation.application.restaurant.imges.service.RestaurantImageService;
import com.reservation.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/restaurants/images")
@RequiredArgsConstructor
public class RestaurantImageController implements RestauranImageSwagger {

    private final RestaurantImageService imageService;

    @PostMapping("/{restaurantId}/upload")
    public ResponseEntity<ApiResponse<String>> uploadImage(
            @PathVariable Long restaurantId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") boolean isThumbnail) {

        String imageUrl = imageService.uploadImage(restaurantId, file, isThumbnail);

        return ResponseEntity.ok(ApiResponse.success(imageUrl));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<?>> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok(ApiResponse.success("이미지 삭제 완료"));
    }
}