package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 保险理赔实体类
 * 用于管理宠物问诊费用的保险理赔信息
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "insurance_claim")
@Schema(description = "保险理赔")
public class InsuranceClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "理赔ID")
    private Long id;

    @Column(length = 100)
    @Schema(description = "理赔编号")
    private String claimNo;

    @Schema(description = "问诊记录ID")
    private Long consultationId;

    @Column(length = 100)
    @Schema(description = "问诊编号")
    private String consultationNo;

    @Column(length = 100)
    @Schema(description = "保单号")
    private String policyNo;

    @Column(length = 100)
    @Schema(description = "保险公司")
    private String insuranceCompany;

    @Column(length = 100)
    @Schema(description = "宠物名称")
    private String petName;

    @Column(length = 100)
    @Schema(description = "宠主姓名")
    private String ownerName;

    @Column(length = 20)
    @Schema(description = "宠主电话")
    private String ownerPhone;

    @Column(length = 100)
    @Schema(description = "宠主身份证号")
    private String ownerIdCard;

    @Column(precision = 10, scale = 2)
    @Schema(description = "问诊总费用")
    private BigDecimal totalFee;

    @Column(precision = 10, scale = 2)
    @Schema(description = "理赔金额")
    private BigDecimal claimAmount;

    @Column(precision = 10, scale = 2)
    @Schema(description = "自付金额")
    private BigDecimal selfPayAmount;

    @Column(length = 2000)
    @Schema(description = "理赔说明")
    private String claimDescription;

    @Column(length = 2000)
    @Schema(description = "拒绝原因")
    private String rejectReason;

    @Column(length = 50)
    @Schema(description = "理赔状态：PENDING-待审核, APPROVED-已批准, REJECTED-已拒绝, COMPLETED-已完成")
    private String status;

    @Column(length = 200)
    @Schema(description = "审核人")
    private String reviewedBy;

    @Schema(description = "审核时间")
    private LocalDateTime reviewedTime;

    @Schema(description = "理赔完成时间")
    private LocalDateTime completedTime;

    @Schema(description = "申请时间")
    private LocalDateTime applyTime;

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
        if (applyTime == null) {
            applyTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
