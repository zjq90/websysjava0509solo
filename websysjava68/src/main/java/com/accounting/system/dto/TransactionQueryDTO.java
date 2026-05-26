package com.accounting.system.dto;

import com.accounting.system.common.PageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易记录查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "交易记录查询")
public class TransactionQueryDTO extends PageQuery {

    @ApiModelProperty(value = "交易类型：INCOME-收入，EXPENSE-支出", example = "EXPENSE")
    private String transactionType;

    @ApiModelProperty(value = "账户ID", example = "1")
    private Long accountId;

    @ApiModelProperty(value = "分类ID", example = "11")
    private Long categoryId;

    @ApiModelProperty(value = "标签ID列表")
    private List<Long> tagIds;

    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "关键字（搜索描述/商家）")
    private String keyword;

    @ApiModelProperty(value = "最小金额")
    private java.math.BigDecimal minAmount;

    @ApiModelProperty(value = "最大金额")
    private java.math.BigDecimal maxAmount;
}
