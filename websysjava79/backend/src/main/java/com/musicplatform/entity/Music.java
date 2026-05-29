package com.musicplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "musics")
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private User artist;

    private String artistName;

    private String album;

    private String coverUrl;

    private String audioUrl;

    private String duration;

    private int playCount = 0;
    private int downloadCount = 0;
    private int likeCount = 0;
    private int commentCount = 0;

    private double price = 0.0;
    private boolean isExclusive = false;
    private boolean isPremiumOnly = false;

    @Enumerated(EnumType.STRING)
    private MusicStatus status = MusicStatus.PENDING;

    @ElementCollection
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime publishedAt;

    public enum MusicStatus {
        PENDING, APPROVED, REJECTED, REMOVED
    }
}
