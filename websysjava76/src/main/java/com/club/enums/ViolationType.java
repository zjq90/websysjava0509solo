package com.club.enums;

/**
 * 违规类型枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
public enum ViolationType {

    /**
     * 长期不活动
     */
    INACTIVE("长期不活动"),

    /**
     * 财务问题
     */
    FINANCIAL("财务问题"),

    /**
     * 违规活动
     */
    ILLEGAL_ACTIVITY("违规活动"),

    /**
     * 其他违规
     */
    OTHER("其他违规");

    private final String description;

    ViolationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
