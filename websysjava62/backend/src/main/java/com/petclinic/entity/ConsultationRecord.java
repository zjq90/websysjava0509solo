package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 问诊记录实体类
 * 用于记录宠物问诊的详细信息
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "consultation_record")
@Schema(description = "问诊记录")
public class ConsultationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "问诊ID")
    private Long id;

    @Column(length = 100)
    @Schema(description = "问诊编号")
    private String consultationNo;

    @Column(length = 100)
    @Schema(description = "宠物名称")
    private String petName;

    @Column(length = 50)
    @Schema(description = "宠物类型：DOG-狗, CAT-猫, BIRD-鸟, OTHER-其他")
    private String petType;

    @Column(length = 50)
    @Schema(description = "宠物年龄")
    private String petAge;

    @Column(length = 100)
    @Schema(description = "宠主姓名")
    private String ownerName;

    @Column(length = 20)
    @Schema(description = "宠主电话")
    private String ownerPhone;

    @Column(length = 1000)
    @Schema(description = "主诉症状")
    private String chiefComplaint;

    @Column(length = 2000)
    @Schema(description = "详细症状描述")
    private String symptomDetail;

    @Column(length = 2000)
    @Schema(description = "诊断结果")
    private String diagnosisResult;

    @Column(length = 2000)
    @Schema(description = "治疗方案")
    private String treatmentPlan;

    @Column(length = 2000)
    @Schema(description = "医嘱建议")
    private String doctorAdvice;

    @Column(length = 200)
    @Schema(description = "接诊医生")
    private String doctorName;

    @Column(precision = 10, scale = 2)
    @Schema(description = "问诊费用")
    private BigDecimal consultationFee;

    @Column(length = 50)
    @Schema(description = "问诊状态：PENDING-待诊断, DIAGNOSED-已诊断, COMPLETED-已完成")
    private String status;

    @Schema(description = "是否已理赔")
    private Boolean claimed;

    @Column(length = 100)
    @Schema(description = "理赔编号")
    private String claimNo;

    @Schema(description = "问诊时间")
    private LocalDateTime consultationTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
        if (claimed == null) {
            claimed = false;
        }
        if (consultationTime == null) {
            consultationTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
