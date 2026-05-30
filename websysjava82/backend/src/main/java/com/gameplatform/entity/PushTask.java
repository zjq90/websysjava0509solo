package com.gameplatform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "push_task")
public class PushTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Column(name = "target_filter")
    private String targetFilter;

    @Column(name = "member_level")
    private Integer memberLevel;

    @Column(name = "active_days")
    private Integer activeDays;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;

    @Column(name = "is_timed", nullable = false)
    private Boolean timed = false;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "total_users")
    private Integer totalUsers;

    @Column(name = "delivered_count")
    private Integer deliveredCount = 0;

    @Column(name = "opened_count")
    private Integer openedCount = 0;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @Column(name = "template_id")
    private Long templateId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
