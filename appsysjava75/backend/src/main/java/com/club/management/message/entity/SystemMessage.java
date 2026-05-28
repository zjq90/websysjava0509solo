package com.club.management.message.entity;

import com.club.management.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统消息实体类
 * 包含审核结果通知、活动开始提醒、社团公告、招新进度通知等
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "sys_message")
@EqualsAndHashCode(callSuper = true)
public class SystemMessage extends BaseEntity {

    /**
     * 消息类型 0-审核结果通知 1-活动开始提醒 2-社团公告 3-招新进度通知 4-系统通知
     */
    private Integer messageType;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 接收用户ID（为空表示全体）
     */
    private Long receiverId;

    /**
     * 接收社团ID（按社团推送）
     */
    private Long clubId;

    /**
     * 关联业务ID
     */
    private Long businessId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 是否已读 0-未读 1-已读
     */
    private Integer isRead;

    /**
     * 是否已读人数
     */
    private Integer readCount;

    /**
     * 总人数
     */
    private Integer totalCount;

    /**
     * 是否重要 0-普通 1-重要
     */
    private Integer isImportant;

    /**
     * 推送时间
     */
    private String pushTime;
}
