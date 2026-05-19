package com.pethospital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 在线问诊实体类
 * 存储图文问诊和视频问诊信息，支持AI紧急症状识别
 * 
 * @author Pet Hospital Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long petId;

    private Long doctorId;

    @Column(length = 50)
    private String petName;

    @Column(length = 50)
    private String doctorName;

    @Column(nullable = false, length = 50)
    private String consultationType;

    @Column(length = 100)
    private String symptomType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String symptoms;

    @Column(length = 1000)
    private String symptomImages;

    @Column(length = 500)
    private String symptomVideo;

    private Boolean emergencyFlag = false;

    @Column(length = 500)
    private String emergencySuggestion;

    @Column(columnDefinition = "TEXT")
    private String doctorReply;

    @Column(columnDefinition = "TEXT")
    private String aiDiagnosis;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(length = 200)
    private String videoRoomId;

    private LocalDateTime videoStartTime;

    private LocalDateTime videoEndTime;

    @Column(length = 20)
    private String status = "PENDING";

    private Integer rating;

    @Column(length = 500)
    private String review;

    @Column(updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
