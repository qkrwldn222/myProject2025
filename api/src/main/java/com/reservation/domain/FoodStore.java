package com.reservation.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "food_store" , schema = "service")
public class FoodStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID 설정
    private Long mgtNo; // 관리번호

    @Column(name = "apv_perm_ymd")
    private LocalDate apvPermYmd; // 인허가일자

    @Column(name = "apv_cancel_ymd")
    private LocalDate apvCancelYmd; // 인허가취소일자

    @Column(name = "trd_state_gbn", length = 10)
    private String trdStateGbn; // 영업상태코드

    @Column(name = "trd_state_nm", length = 50)
    private String trdStateNm; // 영업상태명

    @Column(name = "dtl_state_gbn", length = 10)
    private String dtlStateGbn; // 상세영업상태코드

    @Column(name = "dtl_state_nm", length = 50)
    private String dtlStateNm; // 상세영업상태명

    @Column(name = "dcb_ymd")
    private LocalDate dcbYmd; // 폐업일자

    @Column(name = "clg_st_dt")
    private LocalDate clgStDt; // 휴업시작일자

    @Column(name = "clg_end_dt")
    private LocalDate clgEndDt; // 휴업종료일자

    @Column(name = "ropn_ymd")
    private LocalDate ropnYmd; // 재개업일자

    @Column(name = "site_tel", length = 20)
    private String siteTel; // 전화번호

    @Column(name = "site_area")
    private Double siteArea; // 소재지면적

    @Column(name = "site_post_no", length = 10)
    private String sitePostNo; // 소재지우편번호

    @Column(name = "site_whl_addr", length = 255)
    private String siteWhlAddr; // 지번주소

    @Column(name = "rdn_whl_addr", length = 255)
    private String rdnWhlAddr; // 도로명주소

    @Column(name = "rdn_post_no", length = 10)
    private String rdnPostNo; // 도로명우편번호

    @Column(name = "bplc_nm", length = 100)
    private String bplcNm; // 사업장명

    @Column(name = "last_mod_ts")
    private LocalDate lastModTs; // 최종수정일자

    @Column(name = "update_gbn", length = 10)
    private String updateGbn; // 데이터갱신구분

    @Column(name = "update_dt")
    private LocalDate updateDt; // 데이터갱신일자

    @Column(name = "uptae_nm", length = 50)
    private String uptaeNm; // 업태구분명

    @Column(name = "x")
    private Double x; // 좌표정보(X)

    @Column(name = "y")
    private Double y; // 좌표정보(Y)

    @Column(name = "snt_uptae_nm", length = 50)
    private String sntUptaeNm; // 위생업태명

    @Column(name = "man_eip_cnt")
    private Integer manEipCnt; // 남성종사자수

    @Column(name = "wme_ip_cnt")
    private Integer wmeIpCnt; // 여성종사자수

    @Column(name = "trd_pjubns_enm", length = 50)
    private String trdPjubnsEnm; // 영업장주변구분명

    @Column(name = "lvse_nm", length = 50)
    private String lvseNm; // 등급구분명

    @Column(name = "wtr_sply_facil_se_nm", length = 50)
    private String wtrSplyFacilSeNm; // 급수시설구분명

    @Column(name = "tot_ep_num")
    private Integer totEpNum; // 총인원

    @Column(name = "hoff_ep_cnt")
    private Integer hoffEpCnt; // 본사종업원수

    @Column(name = "fcty_owk_ep_cnt")
    private Integer fctyOwkEpCnt; // 공장사무직종업원수

    @Column(name = "fcty_sil_job_ep_cnt")
    private Integer fctySilJobEpCnt; // 공장판매직종업원수

    @Column(name = "fcty_pdt_job_ep_cnt")
    private Integer fctyPdtJobEpCnt; // 공장생산직종업원수

    @Column(name = "bdng_own_se_nm", length = 50)
    private String bdngOwnSeNm; // 건물소유구분명

    @Column(name = "is_ream")
    private Long isReam; // 보증액

    @Column(name = "mon_am")
    private Long monAm; // 월세액

    @Column(name = "mult_usn_upso_yn", columnDefinition = "TINYINT(1)")
    private Boolean multUsnUpsoYn; // 다중이용업소여부

    @Column(name = "facil_tot_scp")
    private Double facilTotScp; // 시설총규모

    @Column(name = "jt_up_so_asgn_no", length = 50)
    private String jtUpSoAsgnNo; // 전통업소지정번호

    @Column(name = "jt_up_so_main_edf", length = 100)
    private String jtUpSoMainEdf; // 전통업소주된음식

    @Column(name = "home_page", length = 255)
    private String homePage; // 홈페이지
}
