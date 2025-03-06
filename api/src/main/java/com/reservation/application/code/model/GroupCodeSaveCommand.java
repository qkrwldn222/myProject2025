package com.reservation.application.code.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Data
@NoArgsConstructor
public class GroupCodeSaveCommand implements BaseValidation {

  @Schema(name = "groupCode", description = "그룹 코드")
  private String groupCode;

  @Schema(name = "useYn", description = "사용여부")
  private String useYn;

  @Schema(name = "description", description = "설명")
  private String description;

  @Override
  public void validate() {
    if (!StringUtils.hasText(groupCode)) {
      throw new ApiException("그룹코드는 필수 값 입니다.");
    }
  }
}
