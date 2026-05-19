package com.petclinic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 专家问答实体类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "expert_questions")
@EqualsAndHashCode(callSuper = true)
public class ExpertQuestion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id", nullable = false)
    private User expert;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "images", length = 1000)
    private String images;

    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";

    @Column(name = "question_time", nullable = false)
    private LocalDateTime questionTime;

    @Column(name = "answer_time")
    private LocalDateTime answerTime;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comment", length = 500)
    private String comment;
}