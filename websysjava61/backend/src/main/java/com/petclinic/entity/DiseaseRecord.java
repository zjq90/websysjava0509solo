package com.petclinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 疾病记录实体类
 */
@Data
@Entity
@Table(name = "disease_record")
public class DiseaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 问诊记录ID
     */
    @Column(nullable = false)
    private Long consultationId;

    /**
     * 疾病名称
     */
    @Column(nullable = false, length = 100)
    private String diseaseName;

    /**
     * 宠物类型（猫、狗等）
     */
    @Column(nullable = false, length = 20)
    private String petType;

    /**
     * 宠物年龄
     */
    @Column(nullable = false)
    private Integer petAge;

    /**
     * 严重程度：1-轻微，2-中等，3-严重
     */
    @Column(nullable = false)
    private Integer severity;

    /**
     * 记录时间
     */
    @Column(nullable = false)
    private LocalDateTime recordTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (recordTime == null) {
            recordTime = LocalDateTime.now();
        }
    }
}
