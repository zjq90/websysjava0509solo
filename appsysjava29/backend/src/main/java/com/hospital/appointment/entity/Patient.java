package com.hospital.appointment.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 就诊人实体类
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "patient")
@Schema(description = "就诊人信息")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "就诊人ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "所属用户ID")
    private Long userId;

    @Column(name = "patient_name", nullable = false, length = 50)
    @Schema(description = "就诊人姓名")
    private String patientName;

    @Column(name = "phone", length = 20)
    @Schema(description = "手机号")
    private String phone;

    @Column(name = "id_card", length = 50)
    @Schema(description = "身份证号")
    private String idCard;

    @Column(name = "gender")
    @Schema(description = "性别")
    private Integer gender;

    @Column(name = "birthday")
    @Schema(description = "生日")
    private LocalDate birthday;

    @Column(name = "relation", length = 20)
    @Schema(description = "关系: self/father/mother/son/daughter/spouse/other")
    private String relation;

    @Column(name = "is_default")
    @Schema(description = "是否默认就诊人")
    private Integer isDefault;

    @Column(name = "insurance_no", length = 50)
    @Schema(description = "医保号")
    private String insuranceNo;

    @Column(name = "allergy_history", columnDefinition = "TEXT")
    @Schema(description = "过敏史")
    private String allergyHistory;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isDefault == null) isDefault = 0;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
