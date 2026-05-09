package com.agricultural.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 首页仪表盘DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private BigDecimal totalSalesAmount;
    private BigDecimal totalPurchaseAmount;
    private BigDecimal totalReceivable;
    private BigDecimal totalPayable;
    private Long pendingOrdersCount;
    private Long unsettledFinanceCount;
    private List<CostSummaryDTO> costSummary;
}
