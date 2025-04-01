package com.reservation.adapter.review.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ReviewUrlRequest extends ReviewRequest {

  @Schema(name = "urls", description = "이미지 업로드 url")
  private List<String> urls;
}
