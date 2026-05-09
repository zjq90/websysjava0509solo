package com.agricultural.entity.enums;

/**
 * 财务账款状态枚举
 */
public enum FinanceStatus {
    
    /**
     * 未结清 - 账款尚未还清
     */
    UNSETTLED("未结清"),
    
    /**
     * 部分结清 - 已还部分款项
     */
    PARTIAL_SETTLED("部分结清"),
    
    /**
     * 已结清 - 账款全部还清
     */
    SETTLED("已结清");
    
    private final String description;
    
    FinanceStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
