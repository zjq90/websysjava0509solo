package com.agricultural.entity.enums;

/**
 * 财务类型枚举
 * 用于区分应收账款和应付账款
 */
public enum FinanceType {
    
    /**
     * 应收账款 - 客户欠款
     */
    RECEIVABLE("应收账款"),
    
    /**
     * 应付账款 - 欠供应商的款
     */
    PAYABLE("应付账款");
    
    private final String description;
    
    FinanceType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
