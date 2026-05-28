package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团通知实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_notification")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团通知")
public class ClubNotification extends BaseEntity {

    @Schema(description = "接收用户ID", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "社团名称", example = "计算机协会")
    @Column(name = "club_name", length = 100)
    private String clubName;

    @Schema(description = "社团Logo", example = "https://example.com/club-logo.png")
    @Column(name = "club_logo", length = 500)
    private String clubLogo;

    @Schema(description = "通知标题", example = "新活动发布")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Schema(description = "通知内容", example = "计算机协会发布了新活动：编程大赛")
    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Schema(description = "通知类型：activity-活动通知 recruit-招新通知 system-系统通知", example = "activity")
    @Column(name = "type", nullable = false, length = 30)
    private String type;

    @Schema(description = "关联ID（活动ID、招新ID等）", example = "1")
    @Column(name = "related_id")
    private Long relatedId;

    @Schema(description = "是否已读：0-未读 1-已读", example = "0")
    @Column(name = "read", nullable = false)
    private Integer read = 0;

    @Schema(description = "读取时间", example = "2024-05-01 12:00:00")
    @Column(name = "read_time", length = 50)
    private String readTime;
}
