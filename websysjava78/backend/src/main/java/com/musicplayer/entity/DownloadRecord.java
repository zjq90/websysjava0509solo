package com.musicplayer.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "download_records")
public class DownloadRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "music_id", nullable = false)
    private Music music;

    @Enumerated(EnumType.STRING)
    @Column(name = "quality", nullable = false)
    private Quality quality;

    @Column(name = "downloaded_at")
    private LocalDateTime downloadedAt;

    public enum Quality {
        QUALITY_128, QUALITY_320, QUALITY_FLAC
    }

    @PrePersist
    protected void onCreate() {
        downloadedAt = LocalDateTime.now();
    }
}
