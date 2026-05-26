package com.accounting.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 账户请求DTO
 */
@Data
@ApiModel(value = "账户请求")
public class AccountDTO {

    @ApiModelProperty(value = "账户ID（编辑时传入）")
    private Long id;

    @NotBlank(message = "账户名称不能为空")
    @Size(max = 50, message = "账户名称长度不能超过50个字符")
    @ApiModelProperty(value = "账户名称", required = true, example = "招商银行储蓄卡")
    private String accountName;

    @NotBlank(message = "账户类型不能为空")
    @ApiModelProperty(value = "账户类型：CASH-现金，BANK_CARD-银行卡，CREDIT_CARD-信用卡，ALIPAY-支付宝，WECHAT-微信钱包，OTHER-其他", required = true, example = "BANK_CARD")
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
