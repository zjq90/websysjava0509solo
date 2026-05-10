package com.appsys.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 订单请求DTO
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Schema(description = "订单请求")
public class OrderDTO {

    @Schema(description = "客户ID")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "优惠金额")
    @DecimalMin(value = "0", message = "优惠金额不能为负数")
    private BigDecimal discountAmount;

    @Schema(description = "订单明细")
    @NotEmpty(message = "订单明细不能为空")
    @Valid
    private List<OrderItemDTO> items;

    @Schema(description = "备注")
    private String remark;
}
