package com.club.enums;

/**
 * 财务类型枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
public enum FinanceType {

    /**
     * 收入
     */
    INCOME("收入"),

    /**
     * 支出
     */
    EXPENSE("支出"),

    /**
     * 报销
     */
    REIMBURSEMENT("报销");

    private final String description;

    FinanceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
