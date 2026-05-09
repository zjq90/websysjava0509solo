package com.agricultural.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 按客户利润分析DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitByCustomerDTO {
    private Long customerId;
    private String customerCode;
    private String customerName;
    private String customerType;
    private String region;
    private BigDecimal salesAmount;
    private BigDecimal salesCost;
    private BigDecimal purchaseAmount;
    private BigDecimal profit;
    private BigDecimal margin;
    private BigDecimal profitPercentage;
}
