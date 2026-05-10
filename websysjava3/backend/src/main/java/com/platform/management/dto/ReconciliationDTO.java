package com.platform.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 对账DTO
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "对账请求")
public class ReconciliationDTO {

    @NotNull(message = "对账日期不能为空")
    @Schema(description = "对账日期", required = true)
    private LocalDate reconDate;

    @NotBlank(message = "支付渠道不能为空")
    @Schema(description = "支付渠道: WECHAT/ALIPAY/UNIONPAY", required = true)
    private String payChannel;

    @Schema(description = "对账类型: DAILY/MONTHLY")
    private String reconType = "DAILY";
}
