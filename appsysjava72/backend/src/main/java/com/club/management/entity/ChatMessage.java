package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 聊天消息实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("chat_message")
public class ChatMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 发送人ID
     */
    private Long senderId;

    /**
     * 消息类型 text/image/voice
     */
    private String messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 媒体文件地址
     */
    private String mediaUrl;

    /**
     * 语音时长(秒)
     */
    private Integer duration;

    /**
     * 是否已读
     */
    private Integer isRead;
}
