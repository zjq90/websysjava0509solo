package com.agricultural.entity.enums;

/**
 * 订单类型枚举
 * 用于区分销售订单和采购订单
 */
public enum OrderType {
    
    /**
     * 销售订单 - 销售产品给客户
     */
    SALES("销售订单"),
    
    /**
     * 采购订单 - 从供应商采购原材料
     */
    PURCHASE("采购订单");
    
    private final String description;
    
    OrderType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
