package com.bikesystem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 故障上报请求DTO
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Data
@Schema(description = "故障上报请求")
public class FaultReportRequest {

    @Schema(description = "车辆ID", required = true)
    @NotNull(message = "车辆ID不能为空")
    private Long bikeId;

    @Schema(description = "故障类型:BRAKE-刹车失灵,CHAIN-链条脱落,TIRE-轮胎问题,LOCK-锁故障,ELECTRIC-电气故障,OTHER-其他", required = true)
    @NotBlank(message = "故障类型不能为空")
    private String faultType;

    @Schema(description = "故障描述")
    private String faultDescription;

    @Schema(description = "故障图片URLs(多个用逗号分隔)")
    private String imageUrls;

    @Schema(description = "上报位置纬度", required = true)
    @NotNull(message = "纬度不能为空")
    private BigDecimal latitude;

    @Schema(description = "上报位置经度", required = true)
    @NotNull(message = "经度不能为空")
    private BigDecimal longitude;
}
