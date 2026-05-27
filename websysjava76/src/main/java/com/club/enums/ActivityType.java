package com.club.enums;

/**
 * 活动类型枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
public enum ActivityType {

    /**
     * 社团内部活动
     */
    INTERNAL("社团内部活动"),

    /**
     * 校级活动
     */
    SCHOOL_LEVEL("校级活动"),

    /**
     * 跨校活动
     */
    CROSS_SCHOOL("跨校活动");

    private final String description;

    ActivityType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
