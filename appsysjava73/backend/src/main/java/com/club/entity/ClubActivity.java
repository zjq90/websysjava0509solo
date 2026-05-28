package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 社团活动实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_club_activity")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "社团活动信息")
public class ClubActivity extends BaseEntity {

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "社团名称", example = "计算机协会")
    @Column(name = "club_name", length = 100)
    private String clubName;

    @Schema(description = "社团Logo", example = "https://example.com/club-logo.png")
    @Column(name = "club_logo", length = 500)
    private String clubLogo;

    @Schema(description = "活动标题", example = "编程大赛")
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Schema(description = "活动封面", example = "https://example.com/activity-cover.png")
    @Column(name = "cover", length = 500)
    private String cover;

    @Schema(description = "活动内容", example = "面向全校学生的编程大赛，设有多个奖项")
    @Column(name = "content", nullable = false, length = 5000)
    private String content;

    @Schema(description = "活动类型", example = "比赛")
    @Column(name = "type", length = 50)
    private String type;

    @Schema(description = "活动地点", example = "图书馆报告厅")
    @Column(name = "location", length = 200)
    private String location;

    @Schema(description = "活动开始时间", example = "2024-05-01 14:00:00")
    @Column(name = "start_time", nullable = false, length = 50)
    private String startTime;

    @Schema(description = "活动结束时间", example = "2024-05-01 18:00:00")
    @Column(name = "end_time", length = 50)
    private String endTime;

    @Schema(description = "报名截止时间", example = "2024-04-25 23:59:59")
    @Column(name = "signup_deadline", length = 50)
    private String signupDeadline;

    @Schema(description = "人数上限", example = "100")
    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Schema(description = "已报名人数", example = "80")
    @Column(name = "participant_count", nullable = false)
    private Integer participantCount = 0;

    @Schema(description = "浏览次数", example = "500")
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Schema(description = "联系人", example = "张三")
    @Column(name = "contact_name", length = 50)
    private String contactName;

    @Schema(description = "联系电话", example = "13800138000")
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Schema(description = "状态：0-未开始 1-进行中 2-已结束", example = "1")
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    @Schema(description = "是否发布通知给关注者：0-否 1-是", example = "1")
    @Column(name = "notify_followers", nullable = false)
    private Integer notifyFollowers = 1;
}
