package com.agricultural.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 按品种利润分析DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitByVarietyDTO {
    private Long varietyId;
    private String varietyCode;
    private String varietyName;
    private BigDecimal salesAmount;
    private BigDecimal salesCost;
    private BigDecimal allocatedCost;
    private BigDecimal totalCost;
    private BigDecimal profit;
    private BigDecimal profitRate;
    private BigDecimal margin;
    private BigDecimal profitPercentage;
}
