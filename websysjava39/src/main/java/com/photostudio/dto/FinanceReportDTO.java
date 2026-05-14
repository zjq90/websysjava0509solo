package com.photostudio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 财务报表DTO
 * 包含收支统计数据
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Schema(description = "财务报表数据")
public class FinanceReportDTO {

    @Schema(description = "报表日期")
    private LocalDate reportDate;

    @Schema(description = "总收入")
    private BigDecimal totalIncome;

    @Schema(description = "总支出")
    private BigDecimal totalExpense;

    @Schema(description = "净利润")
    private BigDecimal netProfit;

    @Schema(description = "订单收入")
    private BigDecimal orderIncome;

    @Schema(description = "工资支出")
    private BigDecimal salaryExpense;

    @Schema(description = "房租支出")
    private BigDecimal rentExpense;

    @Schema(description = "耗材支出")
    private BigDecimal materialsExpense;

    @Schema(description = "其他支出")
    private BigDecimal otherExpense;
}
