package com.agricultural.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 按区域利润分析DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitByRegionDTO {
    private String province;
    private String city;
    private String regionName;
    private Integer customerCount;
    private BigDecimal salesAmount;
    private BigDecimal salesCost;
    private BigDecimal purchaseAmount;
    private BigDecimal profit;
    private BigDecimal margin;
    private Integer orderCount;
    private BigDecimal profitPercentage;
}
