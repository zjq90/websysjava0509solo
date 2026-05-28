package com.club.management.message.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 群成员实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "chat_group_member")
@EqualsAndHashCode(callSuper = true)
public class ChatGroupMember extends BaseEntity {

    /**
     * 群ID
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
     * 群昵称
     */
    private String nickname;

    /**
     * 角色 0-普通成员 1-管理员 2-群主
     */
    private Integer role;

    /**
     * 未读消息数
     */
    private Integer unreadCount;

    /**
     * 是否免打扰 0-否 1-是
     */
    private Integer isMute;

    /**
     * 是否置顶 0-否 1-是
     */
    private Integer isTop;

    /**
     * 状态 0-正常 1-已退出
     */
    private Integer status;
}
