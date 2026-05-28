package com.club.management.entity.enums;

/**
 * 签到状态枚举
 * 
 * @author club-management
 * @version 1.0.0
 */
public enum SignInStatus {

    /**
     * 未签到
     */
    NOT_SIGNED,

    /**
     * 已签到
     */
    SIGNED,

    /**
     * 迟到
     */
    LATE,

    /**
     * 早退
     */
    EARLY_LEAVE,

    /**
     * 缺席
     */
    ABSENT,

    /**
     * 补签
     */
    MAKE_UP
}
