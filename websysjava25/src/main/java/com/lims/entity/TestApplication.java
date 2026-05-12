package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 检验检查申请实体类
 * 存储临床科室提交的检验/检查申请信息
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_application")
@Schema(description = "检验检查申请")
public class TestApplication {

    /**
     * 申请ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "申请ID", example = "1")
    private Long id;

    /**
     * 申请编号，唯一标识
     */
    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "申请编号", example = "APP20240101001")
    private String applicationNo;

    /**
     * 患者ID
     */
    @Column(nullable = false)
    @Schema(description = "患者ID", example = "1")
    private Long patientId;

    /**
     * 申请科室ID
     */
    @Column(nullable = false)
    @Schema(description = "申请科室ID", example = "1")
    private Long applyDepartmentId;

    /**
     * 申请医生ID
     */
    @Column(nullable = false)
    @Schema(description = "申请医生ID", example = "1")
    private Long applyDoctorId;

    /**
     * 执行科室ID（检验/检查科室）
     */
    @Column(nullable = false)
    @Schema(description = "执行科室ID", example = "2")
    private Long executeDepartmentId;

    /**
     * 项目ID
     */
    @Column(nullable = false)
    @Schema(description = "项目ID", example = "1")
    private Long itemId;

    /**
     * 申请类型：LABORATORY-检验，EXAMINATION-检查
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "申请类型", example = "LABORATORY")
    private String applicationType;

    /**
     * 临床诊断
     */
    @Column(length = 500)
    @Schema(description = "临床诊断", example = "上呼吸道感染")
    private String clinicalDiagnosis;

    /**
     * 申请备注
     */
    @Column(length = 500)
    @Schema(description = "申请备注", example = "加急")
    private String remarks;

    /**
     * 状态：PENDING-待确认，CONFIRMED-已确认，ASSIGNED-已分配，PROCESSING-处理中，REPORTED-已出报告，AUDITED-已审核，PUBLISHED-已发布，CANCELLED-已取消
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "状态", example = "PENDING")
    private String status;

    /**
     * 分配的技师ID
     */
    @Column
    @Schema(description = "分配的技师ID", example = "2")
    private Long assignedTechnicianId;

    /**
     * 确认人ID
     */
    @Column
    @Schema(description = "确认人ID", example = "3")
    private Long confirmedBy;

    /**
     * 确认时间
     */
    @Column
    @Schema(description = "确认时间")
    private LocalDateTime confirmedTime;

    /**
     * 申请时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "申请时间")
    private LocalDateTime applyTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        applyTime = LocalDateTime.now();
        status = "PENDING";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
