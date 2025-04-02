package com.reservation.common.file;

import static org.assertj.core.api.Assertions.assertThat;

import com.reservation.common.service.LocalFileStorageService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "file.upload-dir=C:/image") // 테스트용 경로 지정
public class LocalFileStorageServiceTest {

  @InjectMocks private LocalFileStorageService localFileStorageService;

  @BeforeEach
  void setUp() throws IOException {
    // 테스트 환경에서만 사용할 업로드 폴더 설정
    Files.createDirectories(Paths.get("C:/myproject/uploads/review-images"));
  }

  @Test
  void testUploadImage() throws IOException {
    // Given
    MockMultipartFile file =
        new MockMultipartFile(
            "file", "test-image.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());

    // When
    String uploadedFileUrl = localFileStorageService.uploadImage(file, "review-images");

    // Then
    assertThat(uploadedFileUrl).startsWith("/uploads/");
    System.out.println("Uploaded file URL: " + uploadedFileUrl);
  }

  @Test
  void testDeleteImage() {
    // Given
    String imageUrl = "/uploads/review-images/test-image.jpg";

    // When
    localFileStorageService.deleteImage(imageUrl);

    // Then
    Path filePath = Paths.get("C:/myproject/uploads/review-images/test-image.jpg");
    assertThat(Files.exists(filePath)).isFalse();
  }
}
