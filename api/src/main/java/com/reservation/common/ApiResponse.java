package com.reservation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private final T data;

    // 성공 응답 (데이터 있음)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }
}
