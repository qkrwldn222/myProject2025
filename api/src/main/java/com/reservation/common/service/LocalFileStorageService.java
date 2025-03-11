package com.reservation.common.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileStorageService {
    private final static Logger logger = LoggerFactory.getLogger(LocalFileStorageService.class);

    @Value("${file.upload-dir}")
    private String uploadDir;



    public String uploadImage(MultipartFile file, String path) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + path).resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File uploaded {}", fileName);
            return "/uploads/" + fileName; // 클라이언트가 접근할 수 있는 URL 반환
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(imageUrl.replace("/uploads/", ""));
            Files.deleteIfExists(filePath);
            logger.info("File deleted {}", imageUrl);
        } catch (IOException e) {
            throw new RuntimeException("이미지 삭제 실패", e);
        }
    }
}
