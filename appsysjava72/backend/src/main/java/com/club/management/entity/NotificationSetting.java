package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息通知设置实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("notification_setting")
public class NotificationSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 活动提醒 0关闭 1开启
     */
    private Integer activityReminder;

    /**
     * 社团公告 0关闭 1开启
     */
    private Integer clubAnnouncement;

    /**
     * 新成员通知 0关闭 1开启
     */
    private Integer newMemberNotice;

    /**
     * 签到提醒 0关闭 1开启
     */
    private Integer activitySignInReminder;

    /**
     * 聊天消息通知 0关闭 1开启
     */
    private Integer chatMessageNotice;
}
