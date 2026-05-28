package com.club.management.entity.enums;

/**
 * 活动状态枚举
 * 
 * @author club-management
 * @version 1.0.0
 */
public enum ActivityStatus {

    /**
     * 草稿
     */
    DRAFT,

    /**
     * 报名中
     */
    REGISTRATION_OPEN,

    /**
     * 报名结束
     */
    REGISTRATION_CLOSED,

    /**
     * 活动进行中
     */
    ONGOING,

    /**
     * 活动已结束
     */
    COMPLETED,

    /**
     * 已取消
     */
    CANCELLED
}
