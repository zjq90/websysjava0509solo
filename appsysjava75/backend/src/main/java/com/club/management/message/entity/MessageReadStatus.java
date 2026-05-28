package com.club.management.message.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息已读状态实体类
 * 用于记录重要通知的成员已读未读状态
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "message_read_status")
@EqualsAndHashCode(callSuper = true)
public class MessageReadStatus extends BaseEntity {

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 消息类型 0-系统消息 1-群消息
     */
    private Integer messageType;

    /**
     * 群ID（群消息时使用）
     */
    private Long groupId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否已读 0-未读 1-已读
     */
    private Integer isRead;

    /**
     * 阅读时间
     */
    private String readTime;

    /**
     * 是否已提醒 0-否 1-是
     */
    private Integer isReminded;

    /**
     * 提醒次数
     */
    private Integer remindCount;
}
