package com.bikesystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 骑行费用响应DTO
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@Schema(description = "骑行费用响应")
public class RideCostResponse {

    @Schema(description = "骑行记录ID")
    private Long rideRecordId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "骑行时长(分钟)")
    private Integer durationMinutes;

    @Schema(description = "骑行距离(公里)")
    private BigDecimal distanceKm;

    @Schema(description = "基础费用")
    private BigDecimal basePrice;

    @Schema(description = "时长费用")
    private BigDecimal timePrice;

    @Schema(description = "高峰附加费")
    private BigDecimal peakSurcharge;

    @Schema(description = "优惠金额")
    private BigDecimal discountAmount;

    @Schema(description = "总费用")
    private BigDecimal totalAmount;

    @Schema(description = "实付金额")
    private BigDecimal actualAmount;

    @Schema(description = "是否高峰时段")
    private Boolean isPeakHour;

    @Schema(description = "支付状态")
    private String paymentStatus;
}
