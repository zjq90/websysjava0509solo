package com.club.management.entity.enums;

/**
 * 报名状态枚举
 * 
 * @author club-management
 * @version 1.0.0
 */
public enum RegistrationStatus {

    /**
     * 待审核
     */
    PENDING,

    /**
     * 已通过
     */
    APPROVED,

    /**
     * 已拒绝
     */
    REJECTED,

    /**
     * 已取消
     */
    CANCELLED
}
