package com.appsys.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单明细DTO
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Data
@Schema(description = "订单明细")
public class OrderItemDTO {

    @Schema(description = "库存ID")
    @NotNull(message = "库存ID不能为空")
    private Long inventoryId;

    @Schema(description = "销售数量")
    @NotNull(message = "销售数量不能为空")
    @DecimalMin(value = "0.01", message = "销售数量必须大于0")
    private BigDecimal quantity;

    @Schema(description = "销售单价")
    @NotNull(message = "销售单价不能为空")
    @DecimalMin(value = "0", message = "销售单价不能为负数")
    private BigDecimal unitPrice;

    @Schema(description = "备注")
    private String remark;
}
