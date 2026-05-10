package com.appsys.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 订单创建请求DTO
 * 用于接收前端创建订单的请求数�? * 
 * @author appsys-team
 * @version 1.0.0
 */
@Data
@Schema(description = "订单创建请求")
public class OrderCreateRequest {

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    @Schema(description = "客户ID", example = "1", required = true)
    private Long customerId;

    /**
     * 业务员ID
     */
    @Schema(description = "业务员ID", example = "1")
    private Long salespersonId;

    /**
     * 业务员姓�?     */
    @Schema(description = "业务员姓�?, example = "李四")
    private String salespersonName;

    /**
     * 收货地址
     */
    @Schema(description = "收货地址", example = "北京市朝阳区XX街道XX�?)
    private String shippingAddress;

    /**
     * 收货人手机号
     */
    @Schema(description = "收货人手机号", example = "13800138000")
    private String receiverPhone;

    /**
     * 收货人姓�?     */
    @Schema(description = "收货人姓�?, example = "张三")
    private String receiverName;

    /**
     * 订单备注
     */
    @Schema(description = "订单备注", example = "请尽快发�?)
    private String remarks;

    /**
     * 订单项列�?     */
    @NotEmpty(message = "订单项不能为�?)
    @Schema(description = "订单项列�?, required = true)
    private List<OrderItemRequest> items;

    /**
     * 兼容前端orderItems字段�?     * @return 订单项列�?     */
    public List<OrderItemRequest> getOrderItems() {
        return this.items;
    }

    /**
     * 兼容前端orderItems字段�?     * @param orderItems 订单项列�?     */
    public void setOrderItems(List<OrderItemRequest> orderItems) {
        this.items = orderItems;
    }

    /**
     * 兼容前端notes字段�?     * @return 备注
     */
    public String getNotes() {
        return this.remarks;
    }

    /**
     * 兼容前端notes字段�?     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.remarks = notes;
    }

    /**
     * 订单项请求内部类
     */
    @Data
    @Schema(description = "订单项请�?)
    public static class OrderItemRequest {
        
        /**
         * 产品ID
         */
        @NotNull(message = "产品ID不能为空")
        @Schema(description = "产品ID", example = "1", required = true)
        private Long productId;
        
        /**
         * 购买数量
         */
        @NotNull(message = "购买数量不能为空")
        @Schema(description = "购买数量", example = "10", required = true)
        private Integer quantity;
    }
}
