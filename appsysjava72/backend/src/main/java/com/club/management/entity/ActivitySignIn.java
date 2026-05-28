package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 活动签到实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("activity_sign_in")
public class ActivitySignIn extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 签到时间
     */
    private LocalDateTime signInTime;

    /**
     * 签到类型 1扫码 2手动
     */
    private Integer signInType;

    /**
     * 是否离线签到 0否 1是
     */
    private Integer isOffline;

    /**
     * 同步状态 0未同步 1已同步
     */
    private Integer syncStatus;

    /**
     * 签到地点
     */
    private String location;
}
