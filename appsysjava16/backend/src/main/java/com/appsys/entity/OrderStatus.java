package com.appsys.entity;

/**
 * 订单状态枚举
 * 定义销售订单在整个生命周期中的各种状态
 * 
 * @author appsys-team
 * @version 1.0.0
 */
public enum OrderStatus {
    /**
     * 待确认：订单刚创建，等待客户确认
     */
    PENDING_CONFIRMATION("待确认"),
    
    /**
     * 待签署：已确认，等待客户签署电子合同
     */
    PENDING_SIGNATURE("待签署"),
    
    /**
     * 已签署：客户已签署合同
     */
    SIGNED("已签署"),
    
    /**
     * 备货中：已同步ERP，仓库正在备货
     */
    STOCK_PREPARING("备货中"),
    
    /**
     * 已发货：商品已发出
     */
    SHIPPED("已发货"),
    
    /**
     * 已签收：客户已签收
     */
    DELIVERED("已签收"),
    
    /**
     * 已完成：订单完成
     */
    COMPLETED("已完成"),
    
    /**
     * 已取消：订单取消
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
