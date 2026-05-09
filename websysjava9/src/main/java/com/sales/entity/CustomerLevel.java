package com.sales.entity;

/**
 * 客户等级枚举类
 * 用于客户分级管理，不同等级享受不同的价格策略
 */
public enum CustomerLevel {
    /**
     * 普通客户 - 基础等级
     */
    NORMAL("普通"),
    
    /**
     * 优质客户 - 中等等级
     */
    GOOD("优质"),
    
    /**
     * 核心客户 - 最高等级
     */
    CORE("核心");

    private final String description;

    CustomerLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
