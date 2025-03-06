package com.reservation.infrastructure.code.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@Alias("GroupCodeDTO")
public class GroupCodeDTO {
  private Long id;
  private String groupCode;
  private String useYn;
  private String description;
  private LocalDateTime createAt;
  private String createBy;
  private LocalDateTime updateAt;
  private String updateBy;
}
