package com.reservation.application.code.model;

import com.reservation.common.config.ApiException;
import com.reservation.common.valid.BaseValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Data
@NoArgsConstructor
public class GroupCodeUpdateCommand implements BaseValidation {
  @Schema(name = "id", description = "기본키")
  private Long id;

  @Schema(name = "useYn", description = "사용여부")
  private String useYn;

  @Schema(name = "description", description = "설명")
  private String description;

  @Override
  public void validate() {
    if (ObjectUtils.isEmpty(id)) {
      throw new ApiException("ID는 필수 값 입니다.");
    }
  }
}
