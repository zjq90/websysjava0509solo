package com.agricultural.entity.enums;

/**
 * 成本类别枚举
 * 用于多角度核算成本
 */
public enum CostCategory {
    
    /**
     * 育种成本 - 种子培育、研发等费用
     */
    BREEDING("育种成本"),
    
    /**
     * 田间投入 - 农药、化肥、农机等田间作业费用
     */
    FIELD_INPUT("田间投入"),
    
    /**
     * 加工费用 - 清洗、筛选、烘干、包装加工等费用
     */
    PROCESSING("加工费用"),
    
    /**
     * 包装物流 - 包装材料、运输、仓储等费用
     */
    PACKAGING_LOGISTICS("包装物流"),
    
    /**
     * 管理费用 - 管理、办公、人力等间接费用
     */
    ADMINISTRATIVE("管理费用");
    
    private final String description;
    
    CostCategory(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
