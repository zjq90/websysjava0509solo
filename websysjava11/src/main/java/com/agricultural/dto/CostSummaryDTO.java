package com.agricultural.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 成本汇总DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CostSummaryDTO {
    private String category;
    private String categoryName;
    private BigDecimal totalAmount;
    private BigDecimal percentage;
}
