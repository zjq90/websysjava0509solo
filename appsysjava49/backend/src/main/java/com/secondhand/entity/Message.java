package com.secondhand.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer type = 0;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String content;

    private Long relatedId;

    @Column(length = 50)
    private String relatedType;

    private Long senderId;

    @Column(length = 50)
    private String senderName;

    @Column(length = 255)
    private String senderAvatar;

    private Boolean isRead = false;

    private LocalDateTime readTime;

    @Column(updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
