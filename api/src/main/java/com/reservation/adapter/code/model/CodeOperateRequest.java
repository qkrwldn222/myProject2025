package com.reservation.adapter.code.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record CodeOperateRequest(
    @Schema(name = "createRequests", description = "생성 요청") List<CodeSaveRequest> saveRequests,
    @Schema(name = "updateRequests", description = "수정 요청") List<CodeUpdateRequest> updateRequests,
    @Schema(name = "deleteRequests", description = "삭제 요청")
        List<CodeDeleteRequest> deleteRequests) {}
