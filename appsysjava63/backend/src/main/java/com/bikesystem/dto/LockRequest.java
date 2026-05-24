package com.bikesystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 锁车请求DTO
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@Schema(description = "锁车请求")
public class LockRequest {

    @Schema(description = "骑行记录ID(为空则取当前进行中的骑行)")
    private Long rideRecordId;

    @Schema(description = "当前纬度", required = true)
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @Schema(description = "当前经度", required = true)
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;

    @Schema(description = "当前位置地址")
    private String address;

    @Schema(description = "骑行距离(公里)")
    private BigDecimal distanceKm;

    @Schema(description = "使用的优惠券ID")
    private Long couponId;
}
