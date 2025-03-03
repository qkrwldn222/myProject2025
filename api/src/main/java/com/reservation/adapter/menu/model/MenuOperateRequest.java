package com.reservation.adapter.menu.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MenuOperateRequest(
                                 @Schema(name = "createRequests" , description = "생성 요청") List<MenuCreateRequest> createRequests,
                                 @Schema(name = "updateRequests" , description = "수정 요청") List<MenuUpdateRequest> updateRequests,
                                 @Schema(name = "deleteRequests" , description = "삭제 요청") List<MenuDeleteRequest> deleteRequests) {

}
