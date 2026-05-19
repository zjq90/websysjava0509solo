package com.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 帖子实体类（养宠经验分享）
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "posts")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "cover_image", length = 255)
    private String coverImage;

    @Column(name = "images", length = 1000)
    private String images;

    @Column(name = "video_url", length = 255)
    private String videoUrl;

    @Column(name = "post_type", nullable = false, length = 20)
    private String postType;

    @Column(name = "tags", length = 200)
    private String tags;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount = 0;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount = 0;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "audit_reason", length = 500)
    private String auditReason;

    @Column(name = "audit_time")
    private java.time.LocalDateTime auditTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditor_id")
    private User auditor;
}