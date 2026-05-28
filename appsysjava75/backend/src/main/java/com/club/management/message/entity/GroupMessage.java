package com.club.management.message.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群聊消息实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "chat_group_message")
@EqualsAndHashCode(callSuper = true)
public class GroupMessage extends BaseEntity {

    /**
     * 群ID
     */
    private Long groupId;

    /**
     * 发送者ID
     */
    private Long senderId;

    /**
     * 发送者姓名
     */
    private String senderName;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 消息类型 0-文字 1-图片 2-文件 3-系统消息
     */
    private Integer messageType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 图片/文件URL
     */
    private String fileUrl;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 已读人数
     */
    private Integer readCount;

    /**
     * 总人数
     */
    private Integer totalCount;

    /**
     * 是否撤回 0-否 1-是
     */
    private Integer isRecall;
}
