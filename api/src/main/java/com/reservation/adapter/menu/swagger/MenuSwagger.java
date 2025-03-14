package com.reservation.adapter.menu.swagger;

import com.reservation.adapter.menu.model.MenuOperateRequest;
import com.reservation.adapter.menu.model.MenuSearchResponse;
import com.reservation.common.response.ApiListResponse;
import com.reservation.common.response.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "메뉴", description = "메뉴 관련 API")
@RequestMapping("/menu")
@SecurityRequirement(name = "BearerAuth")
public interface MenuSwagger {

  @GetMapping("/search")
  @Operation(
      summary = "메뉴 목록 조회",
      description = "이름, 코드, 역할명을 기준으로 메뉴 데이터를 검색합니다.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "메뉴 목록 조회 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiListResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "메뉴 조회 성공",
                      "state": "SUCCESS",
                      "resultList": [
                        {
                          "name": "권한",
                          "code": "auth",
                          "roleName": "ADMIN"
                        },
                      ],
                    }
                    """))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 (예: 파라미터 오류)",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "검색 조건이 유효하지 않습니다.",
                      "statusCode": 400
                    }
                    """))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "서버 내부 오류",
            content =
                @Content(
                    mediaType = "application/json",
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "서버 내부 오류 발생",
                      "statusCode": 500
                    }
                    """)))
      })
  ResponseEntity<ApiListResponse<MenuSearchResponse>> search(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "code", required = false) String code,
      @RequestParam(value = "roleName", required = false) String roleName);

  @Operation(summary = "메뉴 CRUD 요청", description = "메뉴 데이터 저장, 삭제, 수정 요청을 호출합니다.")
  @PostMapping("/operate")
  ResponseEntity<ApiResponseWrapper<Void>> operateMenu(@RequestBody MenuOperateRequest command);
}
