package com.club.entity;

import com.club.enums.ReviewStatus;
import com.club.enums.ReviewType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 内容审查实体类
 * 存储活动描述、海报等内容的敏感词检测记录
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "content_review")
@Schema(description = "内容审查记录")
public class ContentReview extends BaseEntity {

    /**
     * 关联活动ID
     */
    @Column(name = "activity_id", nullable = false)
    @Schema(description = "活动ID", example = "1")
    private Long activityId;

    /**
     * 活动名称
     */
    @Column(name = "activity_name", length = 200)
    @Schema(description = "活动名称", example = "校园歌唱比赛")
    private String activityName;

    /**
     * 社团名称
     */
    @Column(name = "club_name", length = 100)
    @Schema(description = "社团名称", example = "音乐协会")
    private String clubName;

    /**
     * 审查类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "review_type", nullable = false, length = 30)
    @Schema(description = "审查类型", example = "DESCRIPTION")
    private ReviewType reviewType;

    /**
     * 检测字段名
     */
    @Column(name = "field_name", length = 50)
    @Schema(description = "检测字段", example = "description")
    private String fieldName;

    /**
     * 原始内容
     */
    @Column(name = "original_content", columnDefinition = "TEXT")
    @Schema(description = "原始内容")
    private String originalContent;

    /**
     * 检测到的敏感词（JSON格式存储）
     */
    @Column(name = "sensitive_words", length = 500)
    @Schema(description = "检测到的敏感词", example = "[\"敏感词1\",\"敏感词2\"]")
    private String sensitiveWords;

    /**
     * 审查状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Schema(description = "审查状态", example = "PENDING")
    private ReviewStatus status;

    /**
     * 审核人ID
     */
    @Column(name = "reviewer_id")
    @Schema(description = "审核人ID", example = "1")
    private Long reviewerId;

    /**
     * 审核人姓名
     */
    @Column(name = "reviewer_name", length = 50)
    @Schema(description = "审核人姓名", example = "管理员")
    private String reviewerName;

    /**
     * 审核意见
     */
    @Column(name = "review_opinion", length = 500)
    @Schema(description = "审核意见")
    private String reviewOpinion;

    /**
     * 审核时间
     */
    @Column(name = "review_time")
    @Schema(description = "审核时间")
    private LocalDateTime reviewTime;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;
}
