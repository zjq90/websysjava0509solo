package com.appsys.production.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "alert_notification")
public class AlertNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long batchId;

    @Column(nullable = false, length = 50)
    private String stageCode;

    @Column(nullable = false, length = 100)
    private String stageName;

    @Column(nullable = false, length = 50)
    private String alertType;

    @Column(nullable = false, length = 255)
    private String message;

    private Long supervisorId;

    @Column(length = 50)
    private String supervisorName;

    @Column(length = 20)
    private String status;

    private LocalDateTime readTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == null) {
            status = "UNREAD";
        }
    }
}
