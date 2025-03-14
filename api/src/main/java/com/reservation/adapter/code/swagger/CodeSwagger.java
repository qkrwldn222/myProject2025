package com.reservation.adapter.code.swagger;

import com.reservation.adapter.code.model.CodeOperateRequest;
import com.reservation.adapter.code.model.GroupCodeAdaptSearchResponse;
import com.reservation.application.code.model.GroupCodeSaveCommand;
import com.reservation.application.code.model.GroupCodeUpdateCommand;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import com.reservation.domain.Code;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "공통 코드 관리", description = "공통 코드 관련 API")
@RequestMapping("/code")
@SecurityRequirement(name = "BearerAuth")
public interface CodeSwagger {

  @Operation(
      summary = "코드 목록 조회",
      description = "모든 코드를 조회합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "코드 목록 조회 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "코드 목록 조회 성공",
                      "state": "SUCCESS",
                      "result": [
                        {
                          "code": "ACTIVE",
                          "description": "사용 중"
                        },
                        {
                          "code": "INACTIVE",
                          "description": "사용 안함"
                        }
                      ]
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @GetMapping("/search")
  @Parameters({
    @Parameter(name = "groupCode", description = "그룹 코드", example = "STORE_STATE", required = true)
  })
  ResponseEntity<ApiListResponse<Code>> getAllCodes(
      @RequestParam(value = "groupCode", name = "그룹코드") String groupCode);

  @Operation(
      summary = "코드 CRUD",
      description = "공통 코드 CRUD API를 호출합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "코드 CRUD 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "코드가 성공적으로 처리되었습니다.",
                      "state": "SUCCESS"
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @PostMapping("/operate")
  ResponseEntity<ApiResponseWrapper<Void>> operate(@RequestBody CodeOperateRequest request);

  @Operation(
      summary = "그룹 코드 조회",
      description = "그룹 코드 조회 API를 호출합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "그룹 코드 조회 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "그룹 코드 조회 성공",
                      "state": "SUCCESS",
                      "result": [
                        {
                          "groupCode": "STORE_STATE",
                          "description": "매장 상태",
                          "useYn": "Y"
                        },
                        {
                          "groupCode": "ORDER_STATUS",
                          "description": "주문 상태",
                          "useYn": "N"
                        }
                      ]
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @GetMapping("/group-code/search")
  @Parameters({
    @Parameter(name = "groupCode", description = "그룹 코드", example = "STORE_STATE"),
    @Parameter(name = "useYn", description = "사용 여부", example = "Y")
  })
  ResponseEntity<ApiListResponse<GroupCodeAdaptSearchResponse>> searchGroupCode(
      @RequestParam String groupCode, @RequestParam String useYn);

  @Operation(
      summary = "그룹 코드 저장",
      description = "그룹 코드 저장 API를 호출합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "그룹 코드 저장 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "그룹 코드가 성공적으로 저장되었습니다.",
                      "state": "SUCCESS"
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @PostMapping("/group-code/save")
  ResponseEntity<ApiResponseWrapper<Void>> saveGroupCode(@RequestBody GroupCodeSaveCommand request);

  @Operation(
      summary = "그룹 코드 업데이트",
      description = "그룹 코드 업데이트 API를 호출합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "그룹 코드 업데이트 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponseWrapper.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "그룹 코드가 성공적으로 업데이트되었습니다.",
                      "state": "SUCCESS"
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @PostMapping("/group-code/update")
  ResponseEntity<ApiResponseWrapper<Void>> updateGroupCode(
      @RequestBody GroupCodeUpdateCommand request);
}
