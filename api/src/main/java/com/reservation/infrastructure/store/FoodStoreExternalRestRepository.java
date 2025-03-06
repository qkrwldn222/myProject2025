package com.reservation.infrastructure.store;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservation.application.store.repository.FoodStoreExternalRepository;
import com.reservation.common.utils.DateTimeUtils;
import com.reservation.domain.FoodStore;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Repository
@RequiredArgsConstructor
public class FoodStoreExternalRestRepository implements FoodStoreExternalRepository {
  private static final Logger logger =
      LoggerFactory.getLogger(FoodStoreExternalRestRepository.class);

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  @Value("${interface.common-portal.url}")
  private String apiUrl;

  @Value("${interface.common-portal.key}")
  private String apiKey;

  private static final int FETCH_SIZE = 1000;
  private static final String ENDPOINT = "/{key}/json/LOCALDATA_072405_GS/{start}/{end}/";

  // API에서 모든 데이터를 가져오는 메서드 (반복 호출)
  public List<FoodStore> fetchFoodStores() {
    List<FoodStore> allFoodStores = new ArrayList<>();
    int start = 1;
    int end = FETCH_SIZE;

    while (true) {
      List<FoodStore> batch = fetchFoodStores(start, end);
      if (batch.isEmpty()) break; // 더 이상 데이터가 없으면 중지

      allFoodStores.addAll(batch);
      start += FETCH_SIZE;
      end += FETCH_SIZE;
    }

    return allFoodStores;
  }

  // 특정 범위의 데이터를 가져오는 메서드
  private List<FoodStore> fetchFoodStores(int start, int end) {
    String url =
        UriComponentsBuilder.fromHttpUrl(apiUrl + ENDPOINT)
            .buildAndExpand(apiKey, start, end)
            .toUriString();

    try {
      String response = restTemplate.getForObject(url, String.class);
      JsonNode root = objectMapper.readTree(response);

      JsonNode rows = root.path("LOCALDATA_072405_GS").path("row");
      List<FoodStore> foodStores = new ArrayList<>();

      for (JsonNode node : rows) {
        FoodStore foodStore =
            FoodStore.builder()
                .mgtNo(node.path("MGTNO").asText())
                .apvPermYmd(DateTimeUtils.safeParseDate(node.path("APVPERMYMD").asText()))
                .apvCancelYmd(DateTimeUtils.safeParseDate(node.path("APVCANCELYMD").asText()))
                .trdStateGbn(node.path("TRDSTATEGBN").asText())
                .trdStateNm(node.path("TRDSTATENM").asText())
                .dtlStateGbn(node.path("DTLSTATEGBN").asText())
                .dtlStateNm(node.path("DTLSTATENM").asText())
                .dcbYmd(DateTimeUtils.safeParseDate(node.path("DCBYMD").asText()))
                .clgStDt(DateTimeUtils.safeParseDate(node.path("CLGSTDT").asText()))
                .clgEndDt(DateTimeUtils.safeParseDate(node.path("CLGENDDT").asText()))
                .ropnYmd(DateTimeUtils.safeParseDate(node.path("ROPNYMD").asText()))
                .siteTel(node.path("SITETEL").asText())
                .siteArea(node.path("SITEAREA").asDouble())
                .sitePostNo(node.path("SITEPOSTNO").asText())
                .siteWhlAddr(node.path("SITEWHLADDR").asText())
                .rdnWhlAddr(node.path("RDNWHLADDR").asText())
                .rdnPostNo(node.path("RDNPOSTNO").asText())
                .bplcNm(node.path("BPLCNM").asText())
                .lastModTs(DateTimeUtils.safeParseDate(node.path("LASTMODTS").asText()))
                .updateGbn(node.path("UPDATEGBN").asText())
                .updateDt(DateTimeUtils.safeParseDate(node.path("UPDATEDT").asText()))
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
      logger.error("API 호출 실패: " + e.getMessage(), e);
      return new ArrayList<>();
    }
  }
}
