package com.club.management.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 社团活动实体类
 *
 * @author club-management
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("club_activity")
public class ClubActivity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 社团ID
     */
    private Long clubId;

    /**
     * 活动标题
     */
    private String title;

    /**
     * 活动封面
     */
    private String cover;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 活动类型
     */
    private String activityType;

    /**
     * 活动地点
     */
    private String location;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 报名开始时间
     */
    private LocalDateTime signUpStartTime;

    /**
     * 报名结束时间
     */
    private LocalDateTime signUpEndTime;

    /**
     * 最大参与人数 0不限制
     */
    private Integer maxParticipants;

    /**
     * 当前参与人数
     */
    private Integer currentParticipants;

    /**
     * 是否需要签到 0不需要 1需要
     */
    private Integer needSignIn;

    /**
     * 签到二维码
     */
    private String signInQrCode;

    /**
     * 状态 0未开始 1进行中 2已结束 3已取消
     */
    private Integer status;

    /**
     * 发布人ID
     */
    private Long publisherId;
}
