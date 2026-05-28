package com.club.management.entity;

import com.club.management.entity.enums.ActivityStatus;
import com.club.management.entity.enums.RegistrationScope;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 活动实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "activity")
public class Activity extends BaseEntity {

    /**
     * 所属社团ID
     */
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    /**
     * 活动名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 活动开始时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * 活动地点
     */
    @Column(name = "location", nullable = false, length = 200)
    private String location;

    /**
     * 活动名额
     */
    @Column(name = "quota", nullable = false)
    private Integer quota;

    /**
     * 已报名人数
     */
    @Column(name = "registered_count", nullable = false)
    private Integer registeredCount = 0;

    /**
     * 报名开始时间
     */
    @Column(name = "registration_start_time", nullable = false)
    private LocalDateTime registrationStartTime;

    /**
     * 报名结束时间
     */
    @Column(name = "registration_end_time", nullable = false)
    private LocalDateTime registrationEndTime;

    /**
     * 活动要求
     */
    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    /**
     * 活动简介
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 活动海报URL
     */
    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    /**
     * 报名范围
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "registration_scope", nullable = false, length = 30)
    private RegistrationScope registrationScope;

    /**
     * 是否需要审核
     */
    @Column(name = "need_approval", nullable = false)
    private Boolean needApproval = false;

    /**
     * 签到开始时间
     */
    @Column(name = "sign_in_start_time")
    private LocalDateTime signInStartTime;

    /**
     * 签到结束时间
     */
    @Column(name = "sign_in_end_time")
    private LocalDateTime signInEndTime;

    /**
     * 签到二维码Token（用于防止代签）
     */
    @Column(name = "qr_code_token", length = 100)
    private String qrCodeToken;

    /**
     * 二维码过期时间
     */
    @Column(name = "qr_code_expire_time")
    private LocalDateTime qrCodeExpireTime;

    /**
     * 活动状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ActivityStatus status = ActivityStatus.DRAFT;

    /**
     * 活动负责人姓名
     */
    @Column(name = "organizer_name", length = 50)
    private String organizerName;

    /**
     * 活动负责人电话
     */
    @Column(name = "organizer_phone", length = 20)
    private String organizerPhone;

    /**
     * 是否归档
     */
    @Column(name = "archived", nullable = false)
    private Boolean archived = false;

    /**
     * 活动评分（平均）
     */
    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    /**
     * 评分人数
     */
    @Column(name = "rating_count")
    private Integer ratingCount = 0;

    /**
     * 实到人数
     */
    @Column(name = "attended_count")
    private Integer attendedCount = 0;

    /**
     * 签到率
     */
    @Column(name = "attendance_rate")
    private Double attendanceRate = 0.0;
}
