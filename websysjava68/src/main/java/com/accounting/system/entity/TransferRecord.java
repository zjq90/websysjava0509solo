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

/**
 * 转账记录实体类
 * 记录账户间资金流动，如"现金→银行卡"
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("transfer_record")
@ApiModel(value = "转账记录")
public class TransferRecord extends BaseEntity {

    @ApiModelProperty(value = "转出账户ID", example = "1")
    private Long fromAccountId;

    @ApiModelProperty(value = "转入账户ID", example = "2")
    private Long toAccountId;

    @ApiModelProperty(value = "转账金额", example = "1000.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "转账手续费", example = "0.00")
    private BigDecimal transferFee;

    @ApiModelProperty(value = "转账时间")
    private LocalDateTime transferTime;

    @ApiModelProperty(value = "转账说明", example = "提取现金")
    private String description;

    @ApiModelProperty(value = "备注说明")
    private String remark;

    @TableField(exist = false)
    @ApiModelProperty(value = "转出账户名称（非数据库字段）")
    private String fromAccountName;

    @TableField(exist = false)
    @ApiModelProperty(value = "转入账户名称（非数据库字段）")
    private String toAccountName;
}
