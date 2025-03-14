package com.reservation.adapter.restaurant.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class RestaurantOperatingHoursResponse {

    @Schema(description = "운영 시간 ID", example = "20")
    private Long operatingHoursId;

    @Schema(description = "요일", example = "FRI")
    private String dayOfWeek;

    @Schema(description = "오픈 시간 (HH:mm)", example = "18:00")
    private LocalTime openTime;

    @Schema(description = "마감 시간 (HH:mm)", example = "23:00")
    private LocalTime closeTime;

    @Schema(description = "휴무 여부", example = "false")
    private Boolean isHoliday;
}
