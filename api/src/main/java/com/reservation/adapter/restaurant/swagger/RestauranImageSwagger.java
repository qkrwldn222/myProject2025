package com.reservation.adapter.restaurant.swagger;

import com.reservation.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "레스토랑 이미지", description = "레스토랑 이미지 관련 API")
public interface RestauranImageSwagger {

    @Operation(summary = "이미지 업로드", description = "레스토랑 이미지를 업로드합니다.")
    @PostMapping("/{restaurantId}/upload")
    ResponseEntity<ApiResponse<String>> uploadImage(
            @PathVariable Long restaurantId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "isThumbnail", required = false, defaultValue = "false") boolean isThumbnail);

    @Operation(summary = "이미지 삭제", description = "레스토랑 이미지를 삭제합니다.")
    @DeleteMapping("/{imageId}")
    ResponseEntity<ApiResponse<?>> deleteImage(@PathVariable Long imageId);

}
