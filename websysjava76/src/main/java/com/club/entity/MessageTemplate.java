package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 消息模板实体类
 * 存储系统推送消息模板配置
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "message_template")
@Schema(description = "消息模板")
public class MessageTemplate extends BaseEntity {

    /**
     * 模板编码
     */
    @Column(name = "code", nullable = false, unique = true, length = 50)
    @Schema(description = "模板编码", example = "ACTIVITY_REMINDER")
    private String code;

    /**
     * 模板名称
     */
    @Column(name = "name", nullable = false, length = 100)
    @Schema(description = "模板名称", example = "活动提醒")
    private String name;

    /**
     * 模板类型（通知/提醒/审批结果）
     */
    @Column(name = "type", length = 30)
    @Schema(description = "模板类型", example = "REMINDER")
    private String type;

    /**
     * 消息标题
     */
    @Column(name = "title", nullable = false, length = 200)
    @Schema(description = "消息标题", example = "活动即将开始提醒")
    private String title;

    /**
     * 消息内容模板
     */
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @Schema(description = "消息内容模板", example = "您的活动【{activityName}】将于{startTime}开始，请准时参加。")
    private String content;

    /**
     * 消息变量说明
     */
    @Column(name = "variables", length = 1000)
    @Schema(description = "消息变量说明", example = "activityName:活动名称,startTime:开始时间")
    private String variables;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    @Schema(description = "是否启用", example = "true")
    private Boolean enabled = true;

    /**
     * 推送方式（短信/邮件/站内信）
     */
    @Column(name = "push_type", length = 100)
    @Schema(description = "推送方式", example = "站内信,邮件")
    private String pushType;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;
}
