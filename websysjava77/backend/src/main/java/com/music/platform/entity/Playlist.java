package com.music.platform.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "playlists")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "cover_path", length = 500)
    private String coverPath;

    @Column(name = "user_id")
    private Long userId;

    @Column(length = 20)
    private String type;

    @Column(name = "play_count")
    private Long playCount = 0L;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (playCount == null) playCount = 0L;
    }
}
