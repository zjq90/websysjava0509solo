package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 紧急症状库实体类
 * 用于存储兽医专家定期修订的宠物紧急症状信息
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "emergency_symptom")
@Schema(description = "紧急症状")
public class EmergencySymptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "症状ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "症状名称", required = true)
    private String symptomName;

    @Column(length = 500)
    @Schema(description = "症状描述")
    private String description;

    @Column(length = 1000)
    @Schema(description = "紧急处理措施")
    private String emergencyMeasure;

    @Column(length = 50)
    @Schema(description = "严重程度：LOW-低, MEDIUM-中, HIGH-高, CRITICAL-危急")
    private String severityLevel;

    @Column(length = 100)
    @Schema(description = "适用宠物类型")
    private String petType;

    @Column(length = 200)
    @Schema(description = "修订专家")
    private String revisedBy;

    @Schema(description = "修订时间")
    private LocalDateTime revisedTime;

    @Column(length = 20)
    @Schema(description = "状态：ACTIVE-启用, INACTIVE-禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
