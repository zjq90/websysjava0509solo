package com.psyconsult.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(name = "phq9_score")
    private Integer phq9Score;

    @Column(name = "gad7_score")
    private Integer gad7Score;

    @Column(columnDefinition = "TEXT")
    private String answers;

    @Column(length = 500)
    private String summary;

    @ElementCollection
    @CollectionTable(name = "assessment_recommendations", joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "recommendation")
    private java.util.List<String> recommendations;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
