package com.sales.entity;

/**
 * 订单状态枚举类
 * 定义订单全流程跟踪的各个状态
 */
public enum OrderStatus {
    /**
     * 待下单 - 客户意向阶段
     */
    PENDING_ORDER("待下单"),
    
    /**
     * 已下单 - 订单确认
     */
    ORDERED("已下单"),
    
    /**
     * 待收款 - 等待付款
     */
    PENDING_PAYMENT("待收款"),
    
    /**
     * 已收款 - 付款完成
     */
    PAID("已收款"),
    
    /**
     * 待发货 - 准备发货
     */
    PENDING_SHIPMENT("待发货"),
    
    /**
     * 已发货 - 货物发出
     */
    SHIPPED("已发货"),
    
    /**
     * 运输中 - 物流运输
     */
    IN_TRANSIT("运输中"),
    
    /**
     * 已签收 - 订单完成
     */
    DELIVERED("已签收"),
    
    /**
     * 已取消 - 订单取消
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
