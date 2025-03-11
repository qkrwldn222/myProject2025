package com.reservation.application.restaurant.imges.service;

import com.reservation.common.config.ApiException;
import com.reservation.common.service.LocalFileStorageService;
import com.reservation.domain.Restaurant;
import com.reservation.domain.RestaurantImage;
import com.reservation.infrastructure.restaurant.image.repository.RestaurantImageJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RestaurantImageRestService implements RestaurantImageService {

    private final LocalFileStorageService fileStorageService;
    private final RestaurantImageJpaRepository imageRepository;

    private final static String RESTAURANT_FILE_PATH = "\restaurant";

    @Override
    @Transactional
    public String uploadImage(Long restaurantId, MultipartFile file, boolean isThumbnail) {
        String imageUrl = fileStorageService.uploadImage(file, RESTAURANT_FILE_PATH);

        RestaurantImage image = RestaurantImage.builder()
                .restaurant(Restaurant.builder().id(restaurantId).build())
                .imageUrl(imageUrl)
                .isThumbnail(isThumbnail)
                .build();

        imageRepository.save(image);
        return imageUrl;
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {
        RestaurantImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ApiException("이미지를 찾을 수 없습니다."));

        fileStorageService.deleteImage(image.getImageUrl());
        imageRepository.delete(image);
    }
}
