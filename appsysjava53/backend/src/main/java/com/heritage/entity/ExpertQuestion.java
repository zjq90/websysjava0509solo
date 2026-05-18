package com.heritage.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 专家问答实体类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "expert_question")
public class ExpertQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "expert_id", nullable = false)
    private Long expertId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String images;

    @Column(name = "is_anonymous")
    private Integer isAnonymous = 0;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "is_paid")
    private Integer isPaid = 0;

    @Column(name = "answer_status")
    private Integer answerStatus = 0;

    @Column(name = "answer_content", columnDefinition = "TEXT")
    private String answerContent;

    @Column(name = "answer_time")
    private LocalDateTime answerTime;

    private Integer rating;

    @Column(name = "rating_comment", length = 500)
    private String ratingComment;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
