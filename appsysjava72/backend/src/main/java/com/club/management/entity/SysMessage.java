package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统消息实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message")
public class SysMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 接收用户ID
     */
    private Long userId;

    /**
     * 消息类型 system/activity/club/chat
     */
    private String type;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 业务ID
     */
    private Long bizId;

    /**
     * 是否已读 0未读 1已读
     */
    private Integer isRead;
}
