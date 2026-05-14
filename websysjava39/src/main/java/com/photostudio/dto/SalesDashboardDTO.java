package com.photostudio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售业绩看板数据DTO
 * 包含核心指标展示数据
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Schema(description = "销售业绩看板数据")
public class SalesDashboardDTO {

    @Schema(description = "总营收")
    private BigDecimal totalRevenue;

    @Schema(description = "同比增长率")
    private BigDecimal revenueGrowthRate;

    @Schema(description = "订单总数")
    private Integer totalOrders;

    @Schema(description = "订单同比增长率")
    private BigDecimal orderGrowthRate;

    @Schema(description = "客单价")
    private BigDecimal avgOrderValue;

    @Schema(description = "客单价同比增长率")
    private BigDecimal avgOrderGrowthRate;

    @Schema(description = "转化率(%)")
    private BigDecimal conversionRate;

    @Schema(description = "转化率同比增长率")
    private BigDecimal conversionGrowthRate;

    @Schema(description = "本月目标完成率(%)")
    private BigDecimal targetCompletionRate;
}
