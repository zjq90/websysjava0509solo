package com.platform.management.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 仪表盘统计VO
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "仪表盘统计数据")
public class DashboardVO {

    @Schema(description = "今日销售额")
    private BigDecimal todaySales;

    @Schema(description = "今日订单数")
    private Long todayOrders;

    @Schema(description = "待处理异常订单数")
    private Long pendingExceptionOrders;

    @Schema(description = "待审核发票数")
    private Long pendingInvoices;

    @Schema(description = "本月销售额")
    private BigDecimal monthSales;

    @Schema(description = "本月订单数")
    private Long monthOrders;
}
