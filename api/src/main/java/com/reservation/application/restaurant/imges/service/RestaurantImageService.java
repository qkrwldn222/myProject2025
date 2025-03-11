package com.reservation.application.restaurant.imges.service;

import org.springframework.web.multipart.MultipartFile;

public interface RestaurantImageService {
    public String uploadImage(Long restaurantId, MultipartFile file, boolean isThumbnail);
    public void deleteImage(Long imageId);
}
