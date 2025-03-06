package com.reservation.adapter.code.controller;

import com.reservation.adapter.code.mapper.CodeRequestMapper;
import com.reservation.adapter.code.mapper.CodeResponseMapper;
import com.reservation.adapter.code.model.CodeOperateRequest;
import com.reservation.adapter.code.model.GroupCodeAdaptSearchResponse;
import com.reservation.adapter.code.swagger.CodeSwagger;
import com.reservation.application.code.model.GroupCodeSaveCommand;
import com.reservation.application.code.model.GroupCodeSearchCommand;
import com.reservation.application.code.model.GroupCodeSearchResponse;
import com.reservation.application.code.model.GroupCodeUpdateCommand;
import com.reservation.application.code.service.CodeService;
import com.reservation.common.ApiResponse;
import com.reservation.common.config.EnableGlobalExceptionHandling;
import com.reservation.domain.Code;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
@EnableGlobalExceptionHandling
public class CodeRestController implements CodeSwagger {

  private final CodeService codeService;

  @Override
  @GetMapping("/search")
  public ResponseEntity<ApiResponse<List<Code>>> getAllCodes(
      @RequestParam("groupCode") String groupCode) {

    return ResponseEntity.ok(ApiResponse.success(codeService.searchCodes(groupCode)));
  }

  @Override
  @PostMapping("/operate")
  public ResponseEntity<ApiResponse<String>> operate(@RequestBody CodeOperateRequest request) {

    return ResponseEntity.ok(ApiResponse.success("요청이 완료되었습니다."));
  }

  @Override
  @GetMapping("/group-code/search")
  public ResponseEntity<ApiResponse<List<GroupCodeAdaptSearchResponse>>> searchGroupCode(
      @RequestParam(name = "groupCode", required = false) String groupCode,
      @RequestParam(name = "useYn", required = false) String useYn) {
    GroupCodeSearchCommand command =
        CodeRequestMapper.INSTANCE.tosSearchGroupCodeCommand(groupCode, useYn);

    List<GroupCodeSearchResponse> result = codeService.searchGroupCodes(command);

    List<GroupCodeAdaptSearchResponse> response =
        CodeResponseMapper.INSTANCE.toGroupCodeSearchAdaptResponses(result);

    return ResponseEntity.ok(ApiResponse.success(response));
  }

  @Override
  @PostMapping("/group-code/save")
  public ResponseEntity<ApiResponse<String>> saveGroupCode(
      @RequestBody @Valid GroupCodeSaveCommand request) {

    codeService.saveGroupCode(request);

    return ResponseEntity.ok(ApiResponse.success("요청이 완료되었습니다."));
  }

  @Override
  @PostMapping("/group-code/update")
  public ResponseEntity<ApiResponse<String>> updateGroupCode(
      @RequestBody @Valid GroupCodeUpdateCommand request) {

    codeService.updateGroupCode(request);

    return ResponseEntity.ok(ApiResponse.success("요청이 완료되었습니다."));
  }
}
