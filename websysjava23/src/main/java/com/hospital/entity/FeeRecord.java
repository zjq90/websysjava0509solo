package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 费用记录实体类
 * 存储患者住院期间的费用明细
 * 
 * @author Hospital Management System
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "fee_record")
@Schema(description = "费用记录")
public class FeeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "费用ID")
    private Long id;

    @Column(name = "fee_no", unique = true, nullable = false, length = 32)
    @Schema(description = "费用编号")
    private String feeNo;

    @Column(name = "hospitalization_id", nullable = false)
    @Schema(description = "住院ID")
    private Long hospitalizationId;

    @Column(name = "patient_id", nullable = false)
    @Schema(description = "患者ID")
    private Long patientId;

    @Transient
    @Schema(description = "患者姓名")
    private String patientName;

    @Column(name = "fee_type", length = 64)
    @Schema(description = "费用类型：药品/检查/治疗/护理/手术/床位/材料/其他")
    private String feeType;

    @Column(name = "item_name", length = 256)
    @Schema(description = "项目名称")
    private String itemName;

    @Column(name = "item_spec", length = 256)
    @Schema(description = "项目规格")
    private String itemSpec;

    @Column(name = "price", precision = 10, scale = 2)
    @Schema(description = "单价")
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "quantity")
    @Schema(description = "数量")
    private Integer quantity = 1;

    @Column(name = "unit", length = 32)
    @Schema(description = "单位")
    private String unit;

    @Column(name = "amount", precision = 10, scale = 2)
    @Schema(description = "金额")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "operator_id")
    @Schema(description = "操作人ID")
    private Long operatorId;

    @Transient
    @Schema(description = "操作人姓名")
    private String operatorName;

    @Column(name = "order_id")
    @Schema(description = "关联医嘱ID")
    private Long orderId;

    @Column(name = "payment_status", length = 32)
    @Schema(description = "缴费状态：未缴费/已缴费/已退费")
    private String paymentStatus = "未缴费";

    @Column(name = "payment_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "缴费时间")
    private LocalDateTime paymentTime;

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
