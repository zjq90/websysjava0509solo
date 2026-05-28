package com.club.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 活动参与记录实体类
 *
 * @author club-management
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "t_activity_participant")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "活动参与记录")
public class ActivityParticipant extends BaseEntity {

    @Schema(description = "活动ID", example = "1")
    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    @Schema(description = "社团ID", example = "1")
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Schema(description = "用户ID", example = "1")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "用户名", example = "student001")
    @Column(name = "username", length = 50)
    private String username;

    @Schema(description = "真实姓名", example = "张三")
    @Column(name = "real_name", length = 50)
    private String realName;

    @Schema(description = "头像", example = "https://example.com/avatar.png")
    @Column(name = "avatar", length = 500)
    private String avatar;

    @Schema(description = "手机号", example = "13800138000")
    @Column(name = "phone", length = 20)
    private String phone;

    @Schema(description = "报名时间", example = "2024-04-20 10:30:00")
    @Column(name = "signup_time", length = 50)
    private String signupTime;

    @Schema(description = "签到状态：0-未签到 1-已签到", example = "1")
    @Column(name = "checked_in", nullable = false)
    private Integer checkedIn = 0;

    @Schema(description = "签到时间", example = "2024-05-01 13:50:00")
    @Column(name = "checkin_time", length = 50)
    private String checkinTime;
}
