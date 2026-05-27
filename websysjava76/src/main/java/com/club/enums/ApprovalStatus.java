package com.club.enums;

/**
 * 审核状态枚举
 * 用于社团成立、活动申请等需要审核的场景
 *
 * @author Club Management System
 * @version 1.0.0
 */
public enum ApprovalStatus {

    /**
     * 待审核
     */
    PENDING("待审核"),

    /**
     * 审核通过
     */
    APPROVED("审核通过"),

    /**
     * 审核驳回
     */
    REJECTED("审核驳回");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
