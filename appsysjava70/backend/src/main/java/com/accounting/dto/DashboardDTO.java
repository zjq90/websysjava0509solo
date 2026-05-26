package com.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "看板数据DTO")
public class DashboardDTO {

    @Schema(description = "总资产", example = "15000.00")
    private BigDecimal totalAssets;

    @Schema(description = "总负债", example = "0.00")
    private BigDecimal totalLiabilities;

    @Schema(description = "今日收入", example = "0.00")
    private BigDecimal todayIncome;

    @Schema(description = "今日支出", example = "135.50")
    private BigDecimal todayExpense;

    @Schema(description = "今日收支差额", example = "-135.50")
    private BigDecimal todayBalance;

    @Schema(description = "本月收入", example = "8000.00")
    private BigDecimal monthIncome;

    @Schema(description = "本月支出", example = "3500.00")
    private BigDecimal monthExpense;

    @Schema(description = "本月支出月均", example = "116.67")
    private BigDecimal monthAverageExpense;

    @Schema(description = "预算使用百分比", example = "65.5")
    private BigDecimal budgetUsagePercent;

    @Schema(description = "是否预算超支80%", example = "false")
    private Boolean budgetWarning;

    @Schema(description = "异常消费提醒列表")
    private List<AbnormalExpenseDTO> abnormalExpenses;
}
