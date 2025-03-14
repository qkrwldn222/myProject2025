package com.reservation.common.response;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiListResponse<T> {
  private final String message;
  private final String state;
  private final List<T> resultList;
  private final PageInfo pageInfo;

  @Getter
  @RequiredArgsConstructor
  public static class PageInfo {
    private final int currentPage;
    private final int totalPages;
    private final int pageSize;
    private final long totalElements;
  }

  public static <T> ApiListResponse<T> multiResult(String message, List<T> dataList) {
    return new ApiListResponse<>(message, "SUCCESS", dataList, null);
  }

  public static <T> ApiListResponse<T> pagedResult(
      String message,
      List<T> content,
      int currentPage,
      int totalPages,
      int pageSize,
      long totalElements) {
    PageInfo pageInfo =
        new PageInfo(
            currentPage, // 현재 페이지
            totalPages, // 전체 페이지 수
            pageSize, // 한 페이지당 아이템 개수
            totalElements // 전체 아이템 개수
            );
    return new ApiListResponse<>(message, "SUCCESS", content, pageInfo);
  }
}
