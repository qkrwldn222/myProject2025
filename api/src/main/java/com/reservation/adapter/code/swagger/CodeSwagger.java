package com.reservation.adapter.code.swagger;

import com.reservation.adapter.code.model.CodeOperateRequest;
import com.reservation.adapter.code.model.GroupCodeAdaptSearchResponse;
import com.reservation.application.code.model.GroupCodeSaveCommand;
import com.reservation.application.code.model.GroupCodeUpdateCommand;
import com.reservation.common.ApiResponse;
import com.reservation.domain.Code;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공통 코드 관리", description = "공통 코드 관련 API")
@RequestMapping("/code")
@SecurityRequirement(name = "BearerAuth")
public interface CodeSwagger {

  @Operation(summary = "코드 목록 조회", description = "모든 코드를 조회합니다.")
  @GetMapping("/search")
  @Parameters({
    @Parameter(name = "groupCode", description = "그룹 코드", example = "STORE_STATE", required = true),
  })
  ResponseEntity<ApiResponse<List<Code>>> getAllCodes(
      @RequestParam(value = "groupCode", name = "그룹코드") String groupCode);

  @Operation(summary = "코드 CRUD", description = "공통 코드 crud API를 호출합니다.")
  @PostMapping("/operate")
  ResponseEntity<ApiResponse<String>> operate(@RequestBody CodeOperateRequest request);

  @Operation(summary = "그룹코드 조회", description = "그룹 코드 조회 API를 호출합니다.")
  @GetMapping("/group-code/search")
  @Parameters({
    @Parameter(name = "groupCode", description = "그룹 코드", example = "STORE_STATE"),
    @Parameter(name = "useYn", description = "사용 여부", example = "Y"),
  })
  ResponseEntity<ApiResponse<List<GroupCodeAdaptSearchResponse>>> searchGroupCode(
      @RequestParam String groupCode, @RequestParam String useYn);

  @Operation(summary = "그룹코드 저장", description = "그룹 코드 저장 API를 호출합니다.")
  @PostMapping("/group-code/save")
  ResponseEntity<ApiResponse<String>> saveGroupCode(@RequestBody GroupCodeSaveCommand request);

  @Operation(summary = "그룹코드 업데이트", description = "그룹 코드 업데이트 API를 호출합니다.")
  @PostMapping("/group-code/update")
  ResponseEntity<ApiResponse<String>> updateGroupCode(@RequestBody GroupCodeUpdateCommand request);
}
