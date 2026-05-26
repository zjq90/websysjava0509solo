package com.accounting.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 转账记录请求DTO
 */
@Data
@ApiModel(value = "转账记录请求")
public class TransferRecordDTO {

    @ApiModelProperty(value = "转账记录ID（编辑时传入）")
    private Long id;

    @NotNull(message = "转出账户ID不能为空")
    @ApiModelProperty(value = "转出账户ID", required = true, example = "1")
    private Long fromAccountId;

    @NotNull(message = "转入账户ID不能为空")
    @ApiModelProperty(value = "转入账户ID", required = true, example = "2")
    private Long toAccountId;

    @NotNull(message = "转账金额不能为空")
    @DecimalMin(value = "0.01", message = "转账金额必须大于0")
    @ApiModelProperty(value = "转账金额", required = true, example = "1000.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "转账手续费", example = "0.00")
    private BigDecimal transferFee;

    @ApiModelProperty(value = "转账时间", example = "2024-01-05 11:00:00")
    private LocalDateTime transferTime;

    @ApiModelProperty(value = "转账说明", example = "提取现金")
    private String description;

    @ApiModelProperty(value = "备注说明")
    private String remark;
}
