package com.club.management.message.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群聊实体类
 * 每个社团/部门可创建专属群聊
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "chat_group")
@EqualsAndHashCode(callSuper = true)
public class ChatGroup extends BaseEntity {

    /**
     * 群名称
     */
    private String groupName;

    /**
     * 群头像
     */
    private String groupAvatar;

    /**
     * 群简介
     */
    private String groupDesc;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 部门名称
     */
    private String department;

    /**
     * 群主ID
     */
    private Long ownerId;

    /**
     * 群成员数量
     */
    private Integer memberCount;

    /**
     * 群公告
     */
    private String announcement;

    /**
     * 状态 0-正常 1-解散
     */
    private Integer status;

    /**
     * 最后一条消息内容
     */
    private String lastMessage;

    /**
     * 最后一条消息时间
     */
    private String lastMessageTime;
}
