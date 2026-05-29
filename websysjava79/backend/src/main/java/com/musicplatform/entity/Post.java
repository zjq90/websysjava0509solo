package com.musicplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 2000)
    private String content;

    @Enumerated(EnumType.STRING)
    private PostType type;

    private Long musicId;
    private Long playlistId;

    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.PUBLIC;

    private int likeCount = 0;
    private int commentCount = 0;
    private int shareCount = 0;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum PostType {
        TEXT, MUSIC_SHARE, PLAYLIST_SHARE
    }

    public enum Visibility {
        PUBLIC, FOLLOWERS_ONLY, PRIVATE
    }
}
