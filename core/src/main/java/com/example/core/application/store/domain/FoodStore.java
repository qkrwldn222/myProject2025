package com.example.core.application.store.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("FoodStore")
public class FoodStore {
    private Long mgtNo; // 관리번호
    private LocalDate apvPermYmd; // 인허가일자 aa
    private LocalDate apvCancelYmd; // 인허가취소일자
    private String trdStateGbn; // 영업상태코드
    private String trdStateNm; // 영업상태명
    private String dtlStateGbn; // 상세영업상태코드
    private String dtlStateNm; // 상세영업상태명
    private LocalDate dcbYmd; // 폐업일자
    private LocalDate clgStDt; // 휴업시작일자
    private LocalDate clgEndDt; // 휴업종료일자
    private LocalDate ropnYmd; // 재개업일자
    private String siteTel; // 전화번호
    private Double siteArea; // 소재지면적
    private String sitePostNo; // 소재지우편번호
    private String siteWhlAddr; // 지번주소
    private String rdnWhlAddr; // 도로명주소
    private String rdnPostNo; // 도로명우편번호
    private String bplcNm; // 사업장명
    private LocalDate lastModTs; // 최종수정일자
    private String updateGbn; // 데이터갱신구분
    private LocalDate updateDt; // 데이터갱신일자
    private String uptaeNm; // 업태구분명
    private Double x; // 좌표정보(X)
    private Double y; // 좌표정보(Y)
    private String sntUptaeNm; // 위생업태명
    private Integer manEipCnt; // 남성종사자수
    private Integer wmeIpCnt; // 여성종사자수
    private String trdPjubnsEnm; // 영업장주변구분명
    private String lvseNm; // 등급구분명
    private String wtrSplyFacilSeNm; // 급수시설구분명
    private Integer totEpNum; // 총인원
    private Integer hoffEpCnt; // 본사종업원수
    private Integer fctyOwkEpCnt; // 공장사무직종업원수
    private Integer fctySilJobEpCnt; // 공장판매직종업원수
    private Integer fctyPdtJobEpCnt; // 공장생산직종업원수
    private String bdngOwnSeNm; // 건물소유구분명
    private Long isReam; // 보증액
    private Long monAm; // 월세액
    private Boolean multUsnUpsoYn; // 다중이용업소여부
    private Double facilTotScp; // 시설총규모
    private String jtUpSoAsgnNo; // 전통업소지정번호
    private String jtUpSoMainEdf; // 전통업소주된음식
    private String homePage; // 홈페이지
}
