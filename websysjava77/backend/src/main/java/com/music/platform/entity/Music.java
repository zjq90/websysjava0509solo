package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "music")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String artist;

    @Column(length = 200)
    private String album;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "cover_path", length = 500)
    private String coverPath;

    @Column(length = 20)
    private String format;

    @Column(name = "file_size")
    private Long fileSize;

    @Column
    private Double duration;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "play_count")
    private Long playCount = 0L;

    @Column(name = "like_count")
    private Long likeCount = 0L;

    @Column(name = "collect_count")
    private Long collectCount = 0L;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (playCount == null) playCount = 0L;
        if (likeCount == null) likeCount = 0L;
        if (collectCount == null) collectCount = 0L;
    }
}
