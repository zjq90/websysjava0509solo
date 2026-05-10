package com.platform.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 发票开具DTO
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "发票开具请求")
public class InvoiceIssueDTO {

    @NotNull(message = "发票申请ID不能为空")
    @Schema(description = "发票申请ID", required = true)
    private Long id;

    @NotBlank(message = "发票号码不能为空")
    @Schema(description = "发票号码", required = true)
    private String invoiceNo;

    @Schema(description = "发票代码")
    private String invoiceCode;

    @Schema(description = "开票人")
    private String reviewer;
}
