package com.personal.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仪表盘概览数据传输对象
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "仪表盘概览数据")
public class DashboardOverviewDTO {

    @Schema(description = "今日收入")
    private BigDecimal todayIncome;

    @Schema(description = "今日支出")
    private BigDecimal todayExpense;

    @Schema(description = "本周收入")
    private BigDecimal weekIncome;

    @Schema(description = "本周支出")
    private BigDecimal weekExpense;

    @Schema(description = "本月收入")
    private BigDecimal monthIncome;

    @Schema(description = "本月支出")
    private BigDecimal monthExpense;

    @Schema(description = "总资产")
    private BigDecimal totalAssets;

    @Schema(description = "支出分类占比列表")
    private List<CategoryExpenseDTO> categoryExpenseRatio;

    @Schema(description = "账户余额趋势数据")
    private List<BalanceTrendDTO> balanceTrend;
}
