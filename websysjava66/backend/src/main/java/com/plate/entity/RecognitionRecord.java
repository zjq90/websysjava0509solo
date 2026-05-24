package com.plate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recognition_records")
public class RecognitionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String plateNumber;

    @Column(nullable = false)
    private String cameraId;

    private String cameraName;

    @Column(nullable = false)
    private LocalDateTime passTime;

    private Double confidence;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private String anomalyType;

    private Boolean isAnomaly = false;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (passTime == null) {
            passTime = LocalDateTime.now();
        }
    }
}
