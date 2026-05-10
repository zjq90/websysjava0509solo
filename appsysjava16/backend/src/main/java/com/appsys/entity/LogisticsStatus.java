package com.appsys.entity;

/**
 * 物流状态枚举
 * 定义物流跟踪的各种状态
 * 
 * @author appsys-team
 * @version 1.0.0
 */
public enum LogisticsStatus {
    /**
     * 待发货：订单已确认，等待发货 */
    PENDING_SHIPMENT("待发货"),
    
    /**
     * 已揽收：快递已取件 */
    PICKED_UP("已揽收"),
    
    /**
     * 运输中：包裹在途 */
    IN_TRANSIT("运输中"),
    
    /**
     * 派送中：快递员正在派送 */
    OUT_FOR_DELIVERY("派送中"),
    
    /**
     * 已签收：客户已签收 */
    DELIVERED("已签收"),
    
    /**
     * 已退回：包裹被退回 */
    RETURNED("已退回");

    private final String description;

    LogisticsStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
