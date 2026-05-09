package com.sales.entity;

/**
 * 客户类型枚举类
 * 定义系统支持的三种客户类型：农户、合作社、经销商
 */
public enum CustomerType {
    /**
     * 农户 - 直接生产者
     */
    FARMER("农户"),
    
    /**
     * 合作社 - 组织形式的客户
     */
    COOPERATIVE("合作社"),
    
    /**
     * 经销商 - 中间分销商
     */
    DISTRIBUTOR("经销商");

    private final String description;

    CustomerType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
