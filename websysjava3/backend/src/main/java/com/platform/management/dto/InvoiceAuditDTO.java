package com.platform.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 发票审核DTO
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "发票审核请求")
public class InvoiceAuditDTO {

    @NotNull(message = "发票申请ID不能为空")
    @Schema(description = "发票申请ID", required = true)
    private Long id;

    @NotBlank(message = "审核结果不能为空")
    @Schema(description = "审核结果: APPROVED/REJECTED", required = true)
    private String result;

    @Schema(description = "拒绝原因（拒绝时必填）")
    private String rejectReason;

    @Schema(description = "审核人")
    private String reviewer;
}
