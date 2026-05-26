package com.accounting.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易记录请求DTO
 */
@Data
@ApiModel(value = "交易记录请求")
public class TransactionDTO {

    @ApiModelProperty(value = "交易记录ID（编辑时传入）")
    private Long id;

    @NotNull(message = "交易类型不能为空")
    @ApiModelProperty(value = "交易类型：INCOME-收入，EXPENSE-支出", required = true, example = "EXPENSE")
    private String transactionType;

    @NotNull(message = "交易金额不能为空")
    @DecimalMin(value = "0.01", message = "交易金额必须大于0")
    @ApiModelProperty(value = "交易金额", required = true, example = "128.00")
    private BigDecimal amount;

    @NotNull(message = "账户ID不能为空")
    @ApiModelProperty(value = "账户ID", required = true, example = "1")
    private Long accountId;

    @NotNull(message = "分类ID不能为空")
    @ApiModelProperty(value = "分类ID", required = true, example = "11")
    private Long categoryId;

    @ApiModelProperty(value = "交易时间", example = "2024-01-10 12:00:00")
    private LocalDateTime transactionTime;

    @ApiModelProperty(value = "交易描述/备注", example = "午餐")
    private String description;

    @ApiModelProperty(value = "商家名称", example = "外婆家")
    private String merchant;

    @ApiModelProperty(value = "交易地点")
    private String location;

    @ApiModelProperty(value = "是否周期性交易：0-否，1-是", example = "0")
    private Integer isRecurring;

    @ApiModelProperty(value = "周期类型：DAILY-每天，WEEKLY-每周，MONTHLY-每月，YEARLY-每年")
    private String recurringType;

    @ApiModelProperty(value = "附件（小票照片等）")
    private String attachment;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "标签ID列表")
    private List<Long> tagIds;
}
