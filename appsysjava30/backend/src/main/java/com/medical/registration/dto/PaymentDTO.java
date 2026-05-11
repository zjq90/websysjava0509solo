package com.medical.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Schema(description = "支付请求")
public class PaymentDTO {
    
    @Schema(description = "挂号单号", required = true)
    @NotNull(message = "挂号单号不能为空")
    private String registrationNo;
    
    @Schema(description = "支付方式：WECHAT-微信，ALIPAY-支付宝", required = true)
    private String paymentMethod;
}
