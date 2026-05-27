package com.club.enums;

/**
 * 角色类型枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
public enum RoleType {

    /**
     * 超级管理员
     */
    SUPER_ADMIN("超级管理员"),

    /**
     * 校级管理员
     */
    SCHOOL_ADMIN("校级管理员"),

    /**
     * 社团负责人
     */
    CLUB_LEADER("社团负责人");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
