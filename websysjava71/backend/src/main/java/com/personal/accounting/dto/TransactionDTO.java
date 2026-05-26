package com.personal.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录数据传输对象
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "交易记录数据")
public class TransactionDTO {

    @Schema(description = "交易ID")
    private Long id;

    @NotNull(message = "交易类型不能为空")
    @Schema(description = "交易类型（INCOME/EXPENSE）", required = true)
    private String type;

    @NotNull(message = "交易金额不能为空")
    @Positive(message = "交易金额必须大于0")
    @Schema(description = "交易金额", required = true)
    private BigDecimal amount;

    @NotNull(message = "分类ID不能为空")
    @Schema(description = "分类ID", required = true)
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "分类颜色")
    private String categoryColor;

    @NotNull(message = "账户ID不能为空")
    @Schema(description = "账户ID", required = true)
    private Long accountId;

    @Schema(description = "账户名称")
    private String accountName;

    @Schema(description = "交易时间")
    private LocalDateTime transactionTime;

    @Schema(description = "交易描述")
    private String description;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "标签，逗号分隔")
    private String tags;

    @Schema(description = "商家/交易对象")
    private String merchant;

    @Schema(description = "地理位置")
    private String location;

    @Schema(description = "是否为异常交易")
    private Boolean isAbnormal;

    @Schema(description = "异常原因")
    private String abnormalReason;
}
