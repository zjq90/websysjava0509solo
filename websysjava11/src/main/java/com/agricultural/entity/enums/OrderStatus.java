package com.agricultural.entity.enums;

/**
 * 订单状态枚举
 * 用于跟踪订单的生命周期
 */
public enum OrderStatus {
    
    /**
     * 待审核 - 新建订单，等待审核
     */
    PENDING("待审核"),
    
    /**
     * 已确认 - 订单已审核确认
     */
    CONFIRMED("已确认"),
    
    /**
     * 已发货/入库 - 销售订单已发货，采购订单已入库
     */
    SHIPPED("已发货/入库"),
    
    /**
     * 已完成 - 订单全部完成
     */
    COMPLETED("已完成"),
    
    /**
     * 已取消 - 订单被取消
     */
    CANCELLED("已取消");
    
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
