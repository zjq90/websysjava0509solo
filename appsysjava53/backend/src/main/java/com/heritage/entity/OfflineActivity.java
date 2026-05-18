package com.heritage.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 线下活动实体类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "offline_activity")
public class OfflineActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "activity_type", nullable = false, length = 50)
    private String activityType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(columnDefinition = "TEXT")
    private String images;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "sign_up_start")
    private LocalDateTime signUpStart;

    @Column(name = "sign_up_end")
    private LocalDateTime signUpEnd;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Column(length = 200)
    private String organizer;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    private Integer status = 1;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
