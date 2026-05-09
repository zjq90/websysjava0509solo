package com.sales.entity;

/**
 * 价格类型枚举类
 * 定义价格策略的两种类型：折扣定价和固定价格
 */
public enum PriceType {
    /**
     * 折扣定价 - 按基础价格的比例计算
     */
    DISCOUNT("折扣定价"),
    
    /**
     * 固定价格 - 直接指定售价
     */
    FIXED("固定价格");

    private final String description;

    PriceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
