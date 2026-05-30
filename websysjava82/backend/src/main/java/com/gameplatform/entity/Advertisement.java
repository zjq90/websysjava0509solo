package com.gameplatform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "advertisement")
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_slot_id", nullable = false)
    private Long adSlotId;

    @Column(nullable = false)
    private String title;

    @Column(name = "ad_type", nullable = false)
    private String adType;

    @Column(name = "material_url")
    private String materialUrl;

    @Column(name = "jump_url")
    private String jumpUrl;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "display_start_time")
    private LocalTime displayStartTime;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(name = "display_end_time")
    private LocalTime displayEndTime;

    @Column(name = "display_frequency")
    private String displayFrequency;

    @Column(name = "max_impressions")
    private Integer maxImpressions;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    private Integer priority = 0;

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
