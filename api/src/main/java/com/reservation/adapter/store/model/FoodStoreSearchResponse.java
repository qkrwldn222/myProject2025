package com.reservation.adapter.store.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FoodStoreSearchResponse {
    @Schema(name = "홈페이지" ,  description = "www.naver.com")
    private String homePage; // 홈페이지
    @Schema(name = "사업자명" ,  description = "김치찌개집")
    private String bplcNm; // 사업장명
    @Schema(name = "도로명주소" ,  description = "월드컵북로111")
    private String rdnWhlAddr; // 도로명주소
    @Schema(name = "도로명 우편번호" ,  description = "1111")
    private String rdnPostNo; // 도로명우편번호
    @Schema(name = "전화번호" ,  description = "02-777-7777")
    private String siteTel; // 전화번호
    @Schema(name = "관리번호" ,  description = "1212121")
    private Long mgtNo; // 관리번호
}
