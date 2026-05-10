package com.appsys.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 报表数据DTO
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Builder
@Schema(description = "报表数据")
public class ReportDTO {

    @Schema(description = "库存统计")
    private InventoryStats inventoryStats;

    @Schema(description = "订单统计")
    private OrderStats orderStats;

    @Schema(description = "田间记录统计")
    private FieldStats fieldStats;

    @Data
    @Builder
    @Schema(description = "库存统计")
    public static class InventoryStats {
        @Schema(description = "库存总数量")
        private BigDecimal totalQuantity;

        @Schema(description = "库存总价值")
        private BigDecimal totalValue;

        @Schema(description = "库存种类数")
        private Long seedCount;

        @Schema(description = "即将过期库存数")
        private Long expiringCount;

        @Schema(description = "按种子分类的库存列表")
        private List<Map<String, Object>> bySeed;
    }

    @Data
    @Builder
    @Schema(description = "订单统计")
    public static class OrderStats {
        @Schema(description = "订单总数")
        private Long totalOrders;

        @Schema(description = "订单总金额")
        private BigDecimal totalAmount;

        @Schema(description = "待发货订单数")
        private Long pendingOrders;

        @Schema(description = "已完成订单数")
        private Long completedOrders;

        @Schema(description = "按月统计的订单数据")
        private List<Map<String, Object>> monthlyData;
    }

    @Data
    @Builder
    @Schema(description = "田间记录统计")
    public static class FieldStats {
        @Schema(description = "记录总数")
        private Long totalRecords;

        @Schema(description = "地块总数")
        private Long fieldCount;

        @Schema(description = "种植总面积")
        private BigDecimal totalArea;

        @Schema(description = "按作物分类的统计")
        private List<Map<String, Object>> byCrop;
    }
}
