package com.personal.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 月度趋势数据传输对象
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "月度趋势数据")
public class MonthlyTrendDTO {

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "月份")
    private Integer month;

    @Schema(description = "收入金额")
    private BigDecimal income;

    @Schema(description = "支出金额")
    private BigDecimal expense;

    @Schema(description = "净收入")
    private BigDecimal netIncome;
}
