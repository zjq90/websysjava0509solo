package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 病历实体类
 * 存储住院病历信息
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "medical_record")
@Schema(description = "病历")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "病历ID")
    private Long id;

    @Column(name = "record_no", unique = true, nullable = false, length = 32)
    @Schema(description = "病历编号")
    private String recordNo;

    @Column(name = "hospitalization_id", nullable = false)
    @Schema(description = "住院ID")
    private Long hospitalizationId;

    @Column(name = "patient_id", nullable = false)
    @Schema(description = "患者ID")
    private Long patientId;

    @Transient
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "record_type", length = 64)
    @Schema(description = "病历类型：首次病程/病程记录/出院小结/手术记录/查房记录")
    private String recordType;

    @Column(name = "title", length = 256)
    @Schema(description = "标题")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    @Schema(description = "病历内容")
    private String content;

    @Column(name = "chief_complaint", length = 512)
    @Schema(description = "主诉")
    private String chiefComplaint;

    @Column(name = "present_illness", columnDefinition = "TEXT")
    @Schema(description = "现病史")
    private String presentIllness;

    @Column(name = "past_history", length = 1024)
    @Schema(description = "既往史")
    private String pastHistory;

    @Column(name = "personal_history", length = 1024)
    @Schema(description = "个人史")
    private String personalHistory;

    @Column(name = "family_history", length = 1024)
    @Schema(description = "家族史")
    private String familyHistory;

    @Column(name = "physical_examination", columnDefinition = "TEXT")
    @Schema(description = "体格检查")
    private String physicalExamination;

    @Column(name = "auxiliary_examination", columnDefinition = "TEXT")
    @Schema(description = "辅助检查")
    private String auxiliaryExamination;

    @Column(name = "diagnosis", length = 1024)
    @Schema(description = "诊断")
    private String diagnosis;

    @Column(name = "treatment_plan", columnDefinition = "TEXT")
    @Schema(description = "治疗计划")
    private String treatmentPlan;

    @Column(name = "doctor_id")
    @Schema(description = "医生ID")
    private Long doctorId;

    @Transient
    @Schema(description = "医生姓名")
    private String doctorName;

    @Column(name = "status", length = 32)
    @Schema(description = "状态：草稿/已提交/已审核")
    private String status = "草稿";

    @Column(name = "remark", length = 512)
    @Schema(description = "备注")
    private String remark;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
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
