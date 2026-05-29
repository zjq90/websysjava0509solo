package com.musicplatform.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long fromUserId;
    private String fromUserName;
    private String fromUserAvatar;

    private Long targetId;
    private String targetTitle;

    @Column(length = 500)
    private String content;

    private boolean isRead = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum NotificationType {
        FOLLOW, LIKE, COMMENT, MENTION, SYSTEM
    }
}
