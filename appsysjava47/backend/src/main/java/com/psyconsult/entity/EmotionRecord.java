package com.psyconsult.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emotion_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmotionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "consultation_id")
    private Long consultationId;

    @Column(nullable = false)
    private Integer score;

    @Column(length = 100)
    private String mood;

    @Column(length = 500)
    private String keywords;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
