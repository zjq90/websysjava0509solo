package com.club.enums;

/**
 * 社团状态枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
public enum ClubStatus {

    /**
     * 正常运营
     */
    NORMAL("正常运营"),

    /**
     * 警告
     */
    WARNING("警告"),

    /**
     * 暂停运营
     */
    SUSPENDED("暂停运营"),

    /**
     * 已注销
     */
    CANCELLED("已注销");

    private final String description;

    ClubStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
