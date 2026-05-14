package com.photostudio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客户转化漏斗DTO
 * 包含各环节转化数据
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Schema(description = "客户转化漏斗数据")
public class ConversionFunnelDTO {

    @Schema(description = "咨询量")
    private Integer consultingCount;

    @Schema(description = "咨询到下单转化率(%)")
    private BigDecimal consultToOrderRate;

    @Schema(description = "下单量")
    private Integer orderedCount;

    @Schema(description = "下单到拍摄转化率(%)")
    private BigDecimal orderToShootRate;

    @Schema(description = "拍摄完成量")
    private Integer shootCompletedCount;

    @Schema(description = "拍摄到交付转化率(%)")
    private BigDecimal shootToDeliverRate;

    @Schema(description = "交付量")
    private Integer deliveredCount;

    @Schema(description = "总转化率(咨询到交付)(%)")
    private BigDecimal totalConversionRate;

    @Schema(description = "老客户复购率(%)")
    private BigDecimal repeatPurchaseRate;

    @Schema(description = "转介绍率(%)")
    private BigDecimal referralRate;
}
