package com.plate.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "blacklists")
public class Blacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String plateNumber;

    private String reason;

    @Column(columnDefinition = "TEXT")
    private String evidenceImage;

    private LocalDateTime expireAt;

    private Boolean isActive = true;

    private String createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String removeReason;

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
