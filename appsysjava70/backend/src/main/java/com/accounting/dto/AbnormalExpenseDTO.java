package com.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "异常消费DTO")
public class AbnormalExpenseDTO {

    @Schema(description = "账单ID", example = "1")
    private Long billId;

    @Schema(description = "金额", example = "500.00")
    private BigDecimal amount;

    @Schema(description = "分类名称", example = "餐饮")
    private String categoryName;

    @Schema(description = "商家名称", example = "高端餐厅")
    private String merchant;

    @Schema(description = "交易时间")
    private LocalDateTime transactionTime;

    @Schema(description = "月均支出", example = "100.00")
    private BigDecimal averageExpense;

    @Schema(description = "倍数", example = "5.0")
    private BigDecimal multiple;
}
