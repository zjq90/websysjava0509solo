package com.platform.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 异常订单处理DTO
 * 
 * @author platform
 * @version 1.0.0
 */
@Data
@Schema(description = "异常订单处理请求")
public class ExceptionOrderHandleDTO {

    @NotNull(message = "异常订单ID不能为空")
    @Schema(description = "异常订单ID", required = true)
    private Long id;

    @NotBlank(message = "处理方式不能为空")
    @Schema(description = "处理方式: REFUND/RESHIP/OTHER", required = true)
    private String handleType;

    @Schema(description = "处理备注")
    private String handleRemark;

    @Schema(description = "处理人")
    private String handler;
}
