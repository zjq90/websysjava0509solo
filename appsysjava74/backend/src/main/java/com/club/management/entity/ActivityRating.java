package com.club.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 活动评分评价实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "activity_rating", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"activity_id", "user_id"})
})
public class ActivityRating extends BaseEntity {

    /**
     * 活动ID
     */
    @Column(name = "activity_id", nullable = false)
    private Long activityId;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 用户名（冗余）
     */
    @Column(name = "username", length = 50)
    private String username;

    /**
     * 评分（1-5星）
     */
    @Column(name = "rating", nullable = false)
    private Integer rating;

    /**
     * 活动内容评分
     */
    @Column(name = "content_rating")
    private Integer contentRating;

    /**
     * 组织安排评分
     */
    @Column(name = "organization_rating")
    private Integer organizationRating;

    /**
     * 场地环境评分
     */
    @Column(name = "venue_rating")
    private Integer venueRating;

    /**
     * 评价内容
     */
    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    /**
     * 反馈建议
     */
    @Column(name = "suggestion", columnDefinition = "TEXT")
    private String suggestion;

    /**
     * 是否匿名
     */
    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous = false;

    /**
     * 是否显示（审核通过后显示）
     */
    @Column(name = "visible", nullable = false)
    private Boolean visible = true;
}
