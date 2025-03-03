package com.reservation.common.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    ADMIN("00", "관리자"),
    USER("01", "일반 사용자"),
    STORE_OWNER("02", "가게 운영자"),
    GUEST("03", "외부인"); // 권한 없음

    private final String code;
    private final String description;

    /**
     * 코드 값으로 Role 찾기
     */
    public static RoleType fromCode(String code) {
        for (RoleType role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("잘못된 역할 코드: " + code);
    }
}
