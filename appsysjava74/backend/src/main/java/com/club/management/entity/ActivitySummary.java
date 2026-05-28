package com.club.management.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 活动总结实体类
 * 
 * @author club-management
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "activity_summary")
public class ActivitySummary extends BaseEntity {

    /**
     * 活动ID
     */
    @Column(name = "activity_id", nullable = false, unique = true)
    private Long activityId;

    /**
     * 社团ID
     */
    @Column(name = "club_id", nullable = false)
    private Long clubId;

    /**
     * 总结标题
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 活动总结内容
     */
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    /**
     * 活动亮点
     */
    @Column(name = "highlights", columnDefinition = "TEXT")
    private String highlights;

    /**
     * 活动不足
     */
    @Column(name = "shortcomings", columnDefinition = "TEXT")
    private String shortcomings;

    /**
     * 改进建议
     */
    @Column(name = "improvements", columnDefinition = "TEXT")
    private String improvements;

    /**
     * 成果展示
     */
    @Column(name = "achievements", columnDefinition = "TEXT")
    private String achievements;

    /**
     * 活动现场照片URL（多个用逗号分隔）
     */
    @Column(name = "photo_urls", columnDefinition = "TEXT")
    private String photoUrls;

    /**
     * 附件URL（多个用逗号分隔）
     */
    @Column(name = "attachment_urls", columnDefinition = "TEXT")
    private String attachmentUrls;

    /**
     * 是否同步到社团主页
     */
    @Column(name = "sync_to_club_page", nullable = false)
    private Boolean syncToClubPage = true;

    /**
     * 是否公开可见
     */
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    /**
     * 发布时间
     */
    @Column(name = "publish_time")
    private LocalDateTime publishTime;

    /**
     * 发布人ID
     */
    @Column(name = "publish_by")
    private Long publishBy;

    /**
     * 是否发布
     */
    @Column(name = "published", nullable = false)
    private Boolean published = false;
}
