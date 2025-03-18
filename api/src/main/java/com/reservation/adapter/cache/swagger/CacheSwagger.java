package com.reservation.adapter.cache.swagger;

import com.reservation.common.response.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "캐쉬", description = "캐쉬 관련 API")
@RequestMapping("/cache")
public interface CacheSwagger {

  @Operation(
      summary = "메뉴 캐시 초기화",
      description = "메뉴 정보 캐시를 초기화 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "메뉴 캐시 초기화 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "메뉴 정보 캐시를 초기화 했습니다.",
                      "state": "SUCCESS"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("/menu")
  ResponseEntity<ApiResponseWrapper<Void>> clearMenuCache();

  @Operation(
      summary = "공통 코드 캐시 초기화",
      description = "공통 코드 캐시를 초기화 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "공통 코드 캐시 초기화 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "공통 코드 캐시를 초기화 했습니다.",
                      "state": "SUCCESS"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("/code")
  ResponseEntity<ApiResponseWrapper<Void>> clearCodeCache();

  @Operation(
      summary = "로그인 인증 정보 캐시 초기화",
      description = "로그인 인증 정보 캐시를 초기화 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 인증 정보 캐시 초기화 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "로그인 인증 정보 캐시를 초기화 했습니다.",
                      "state": "SUCCESS"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("/auth")
  ResponseEntity<ApiResponseWrapper<Void>> clearAuthCache();

  @Operation(
      summary = "예약 인증 정보 캐시 초기화",
      description = "예약 인증 정보 캐시를 초기화 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "예약 인증 정보 캐시 초기화 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                    {
                      "message": "예약 인증 정보 캐시를 초기화 했습니다.",
                      "state": "SUCCESS"
                    }
                    """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("/reservation")
  ResponseEntity<ApiResponseWrapper<Void>> clearReservationCache();

  @Operation(
      summary = "웨이팅 정보 캐시 초기화",
      description = "웨이팅 정보 캐시를 초기화 합니다.",
      responses = {
        @ApiResponse(
            responseCode = "200",
            description = "웨이팅 정보 정보 캐시 초기화 성공",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                """
                                        {
                                          "message": "웨이팅 정보 캐시를 초기화 했습니다.",
                                          "state": "SUCCESS"
                                        }
                                        """))),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류")
      })
  @SecurityRequirement(name = "BearerAuth")
  @PostMapping("/waiting")
  ResponseEntity<ApiResponseWrapper<Void>> clearWaiting(
      @RequestParam("restaurantId") Long restaurantId);
}
