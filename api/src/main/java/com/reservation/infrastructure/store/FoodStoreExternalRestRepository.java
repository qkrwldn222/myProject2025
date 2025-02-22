package com.reservation.infrastructure.store;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.domain.FoodStore;
import com.reservation.application.store.repository.FoodStoreExternalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FoodStoreExternalRestRepository implements FoodStoreExternalRepository {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${interface.common-portal.url}")
    private String apiUrl;

    @Value("${interface.common-portal.key}")
    private String apiKey;

    private static final String ENDPOINT = "/{key}/json/LOCALDATA_072405_GS/1/1000/";

    // API 호출 및 데이터 변환
    public List<FoodStore> fetchFoodStores() {
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl + ENDPOINT)
                .buildAndExpand(apiKey)
                .toUriString();

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            // JSON 구조에 맞게 데이터 추출
            JsonNode rows = root.path("LOCALDATA_072405_GS").path("row");
            List<FoodStore> foodStores = new ArrayList<>();

            for (JsonNode node : rows) {
                FoodStore foodStore = FoodStore.builder()
                        .mgtNo(node.path("MGTNO").asLong())
                        .apvPermYmd(parseDate(node.path("APVPERMYMD").asText()))
                        .apvCancelYmd(parseDate(node.path("APVCANCELYMD").asText()))
                        .trdStateGbn(node.path("TRDSTATEGBN").asText())
                        .trdStateNm(node.path("TRDSTATENM").asText())
                        .dtlStateGbn(node.path("DTLSTATEGBN").asText())
                        .dtlStateNm(node.path("DTLSTATENM").asText())
                        .dcbYmd(parseDate(node.path("DCBYMD").asText()))
                        .clgStDt(parseDate(node.path("CLGSTDT").asText()))
                        .clgEndDt(parseDate(node.path("CLGENDDT").asText()))
                        .ropnYmd(parseDate(node.path("ROPNYMD").asText()))
                        .siteTel(node.path("SITETEL").asText())
                        .siteArea(node.path("SITEAREA").asDouble())
                        .sitePostNo(node.path("SITEPOSTNO").asText())
                        .siteWhlAddr(node.path("SITEWHLADDR").asText())
                        .rdnWhlAddr(node.path("RDNWHLADDR").asText())
                        .rdnPostNo(node.path("RDNPOSTNO").asText())
                        .bplcNm(node.path("BPLCNM").asText())
                        .lastModTs(parseDate(node.path("LASTMODTS").asText()))
                        .updateGbn(node.path("UPDATEGBN").asText())
                        .updateDt(parseDate(node.path("UPDATEDT").asText()))
                        .uptaeNm(node.path("UPTAENM").asText())
                        .x(node.path("X").asDouble())
                        .y(node.path("Y").asDouble())
                        .sntUptaeNm(node.path("SNTUPTAENM").asText())
                        .manEipCnt(node.path("MANEIPCNT").asInt())
                        .wmeIpCnt(node.path("WMEIPCNT").asInt())
                        .trdPjubnsEnm(node.path("TRDPJUBNSENM").asText())
                        .lvseNm(node.path("LVSENM").asText())
                        .wtrSplyFacilSeNm(node.path("WTRSPLYFACILSENM").asText())
                        .totEpNum(node.path("TOTEPNUM").asInt())
                        .hoffEpCnt(node.path("HOFFEPCNT").asInt())
                        .fctyOwkEpCnt(node.path("FCTYOWKEPCNT").asInt())
                        .fctySilJobEpCnt(node.path("FCTYSILJOBEPCNT").asInt())
                        .fctyPdtJobEpCnt(node.path("FCTYPDTJOBEPCNT").asInt())
                        .bdngOwnSeNm(node.path("BDNGOWNSENM").asText())
                        .isReam(node.path("ISREAM").asLong())
                        .monAm(node.path("MONAM").asLong())
                        .multUsnUpsoYn(node.path("MULTUSNUPSOYN").asBoolean())
                        .facilTotScp(node.path("FACILTOTSCP").asDouble())
                        .jtUpSoAsgnNo(node.path("JTUPSOASGNNO").asText())
                        .jtUpSoMainEdf(node.path("JTUPSOMAINEDF").asText())
                        .homePage(node.path("HOMEPAGE").asText())
                        .build();

                foodStores.add(foodStore);
            }
            return foodStores;

        } catch (Exception e) {
            throw new RuntimeException("API 호출 실패", e);
        }
    }

    // 날짜 변환 (YYYYMMDD → LocalDate)
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        return LocalDate.parse(dateStr, java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
    }

    @Override
    public List<FoodStore> searchFoodStore() {
        return null;
    }
}
