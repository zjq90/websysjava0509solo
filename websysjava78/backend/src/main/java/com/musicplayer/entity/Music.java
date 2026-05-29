package com.musicplayer.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    private String album;

    private String genre;

    private Integer duration;

    private String coverUrl;

    @Column(name = "file_path_128")
    private String filePath128;

    @Column(name = "file_path_320")
    private String filePath320;

    @Column(name = "file_path_flac")
    private String filePathFlac;

    @Column(name = "file_size_128")
    private Long fileSize128;

    @Column(name = "file_size_320")
    private Long fileSize320;

    @Column(name = "file_size_flac")
    private Long fileSizeFlac;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "music")
    private Lyrics lyrics;

    @Column(name = "play_count")
    private Integer playCount = 0;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "is_premium")
    private Boolean isPremium = false;

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
