package com.accounting.system.entity;

import com.accounting.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 交易记录实体类
 * 核心收支记录表，支持收入和支出两种类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("transaction")
@ApiModel(value = "交易记录")
public class Transaction extends BaseEntity {

    @ApiModelProperty(value = "交易类型：INCOME-收入，EXPENSE-支出", example = "EXPENSE")
    private String transactionType;

    @ApiModelProperty(value = "交易金额", example = "128.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "账户ID", example = "1")
    private Long accountId;

    @ApiModelProperty(value = "分类ID", example = "11")
    private Long categoryId;

    @ApiModelProperty(value = "交易时间")
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

    @TableField(exist = false)
    @ApiModelProperty(value = "账户名称（非数据库字段）")
    private String accountName;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类名称（非数据库字段）")
    private String categoryName;

    @TableField(exist = false)
    @ApiModelProperty(value = "分类图标（非数据库字段）")
    private String categoryIcon;

    @TableField(exist = false)
    @ApiModelProperty(value = "标签列表（非数据库字段）")
    private List<Tag> tags;
}
