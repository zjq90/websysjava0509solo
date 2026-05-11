package com.hospital.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 住院费用实体类
 * 住院费用查询和管理
 * 
 * @author hospital
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "hos_hospital_fee")
@Schema(description = "住院费用信息")
public class HospitalFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "费用记录ID")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "入院申请ID")
    private Long admissionId;

    @Column(nullable = false)
    @Schema(description = "患者用户ID")
    private Long userId;

    @Column(length = 50)
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(length = 30)
    @Schema(description = "申请单号")
    private String applicationNo;

    @Column(nullable = false)
    @Schema(description = "费用日期")
    private LocalDate feeDate;

    @Column(length = 20)
    @Schema(description = "费用类型：药费 检查费 治疗费 手术费 护理费 床位费 其他")
    private String feeType;

    @Column(length = 200)
    @Schema(description = "费用项目名称")
    private String itemName;

    @Column(length = 200)
    @Schema(description = "费用项目说明")
    private String itemDesc;

    @Column(precision = 10, scale = 2)
    @Schema(description = "单价")
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column
    @Schema(description = "数量")
    private Integer quantity = 1;

    @Column(precision = 10, scale = 2)
    @Schema(description = "金额")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(length = 50)
    @Schema(description = "开单医生ID")
    private Long doctorId;

    @Column(length = 50)
    @Schema(description = "开单医生姓名")
    private String doctorName;

    @Column(length = 100)
    @Schema(description = "执行科室")
    private String executeDept;

    @Column(nullable = false)
    @Schema(description = "是否已结算：0未结算 1已结算")
    private Integer settled = 0;

    @Column
    @Schema(description = "结算时间")
    private LocalDateTime settleTime;

    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(length = 50)
    @Schema(description = "创建人")
    private String createBy;

    @Column(length = 500)
    @Schema(description = "备注")
    private String remark;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
