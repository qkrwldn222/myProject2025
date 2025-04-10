package com.reservation.common.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class LocalFileStorageService {
  private static final Logger logger = LoggerFactory.getLogger(LocalFileStorageService.class);

  @Value("${file.upload-dir:C:/image}")
  private String uploadDir;

  public String uploadImage(MultipartFile file, String path) {
    try {
      String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
      Path dirPath = Paths.get(uploadDir, path);
      if (!Files.exists(dirPath)) {
        Files.createDirectories(dirPath);
      }
      Path filePath = dirPath.resolve(fileName);
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
      logger.info("File uploaded {}", filePath.toString());

      // 클라이언트가 접근할 수 있는 URL 반환 (서브 경로 포함)
      return "/uploads/" + path.replace("\\", "/") + "/" + fileName;
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
