package com.club.enums;

/**
 * 社团类型枚举
 *
 * @author Club Management System
 * @version 1.0.0
 */
public enum ClubType {

    /**
     * 学术科技类
     */
    ACADEMIC("学术科技类"),

    /**
     * 文化艺术类
     */
    CULTURAL("文化艺术类"),

    /**
     * 体育竞技类
     */
    SPORTS("体育竞技类"),

    /**
     * 志愿服务类
     */
    VOLUNTEER("志愿服务类"),

    /**
     * 创新创业类
     */
    INNOVATION("创新创业类"),

    /**
     * 兴趣爱好类
     */
    HOBBY("兴趣爱好类");

    private final String description;

    ClubType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
