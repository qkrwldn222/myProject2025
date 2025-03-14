package com.reservation.adapter.restaurant.model;

import com.reservation.common.enums.SeatType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RestaurantSeatResponse {

    @Schema(description = "좌석 ID", example = "30")
    private Long seatId;

    @Schema(description = "좌석 타입", example = "TABLE")
    private SeatType seatType;

    @Schema(description = "좌석 번호", example = "S1")
    private String seatNumber;

    @Schema(description = "최대 수용 인원", example = "2")
    private Integer maxCapacity;

    @Schema(description = "예약 가능 여부", example = "true")
    private Boolean isAvailable;
}
