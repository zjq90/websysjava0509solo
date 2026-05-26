package com.accounting.system.entity;

import com.accounting.system.common.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 账户实体类
 * 支持多账户管理：现金、银行卡、信用卡、支付宝、微信钱包等
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("account")
@ApiModel(value = "账户")
public class Account extends BaseEntity {

    @ApiModelProperty(value = "账户名称", example = "招商银行储蓄卡")
    private String accountName;

    @ApiModelProperty(value = "账户类型：CASH-现金，BANK_CARD-银行卡，CREDIT_CARD-信用卡，ALIPAY-支付宝，WECHAT-微信钱包，OTHER-其他", example = "BANK_CARD")
    private String accountType;

    @ApiModelProperty(value = "账户号码（银行卡号/账号）")
    private String accountNumber;

    @ApiModelProperty(value = "账户余额", example = "35800.50")
    private BigDecimal balance;

    @ApiModelProperty(value = "币种", example = "CNY")
    private String currency;

    @ApiModelProperty(value = "账户图标", example = "🏦")
    private String icon;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @ApiModelProperty(value = "是否默认账户：0-否，1-是", example = "0")
    private Integer isDefault;

    @ApiModelProperty(value = "排序顺序", example = "1")
    private Integer sortOrder;
}
