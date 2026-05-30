package com.gameplatform.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    private String icon;

    private String description;

    @Column(name = "is_enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "reward_video_enabled", nullable = false)
    private Boolean rewardVideoEnabled = false;

    @Column(name = "reward_content")
    private String rewardContent;

    @Column(name = "daily_watch_limit")
    private Integer dailyWatchLimit = 10;

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
