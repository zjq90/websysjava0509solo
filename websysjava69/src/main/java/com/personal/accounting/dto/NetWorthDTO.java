package com.personal.accounting.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class NetWorthDTO {
    private BigDecimal totalAssets;
    private BigDecimal totalLiabilities;
    private BigDecimal netWorth;
    private Map<String, BigDecimal> assetBreakdown;
    private Map<String, BigDecimal> liabilityBreakdown;
    private BigDecimal totalInvestments;
    private BigDecimal totalInvestmentProfit;
}
