package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报销实体类
 * 存储员工报销申请和审批记录
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_reimbursement")
@Schema(description = "报销记录")
public class Reimbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "报销ID")
    private Long id;

    @Column(name = "reimburse_no", nullable = false, unique = true, length = 50)
    @Schema(description = "报销编号")
    private String reimburseNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @Schema(description = "申请人")
    private Employee employee;

    @Column(nullable = false, length = 100)
    @Schema(description = "报销标题")
    private String title;

    @Column(length = 500)
    @Schema(description = "报销说明")
    private String description;

    /**
     * 报销分类：TRAVEL-差旅费，TRANSPORT-交通费，MEAL-餐费，OFFICE-办公费，OTHER-其他
     */
    @Column(nullable = false, length = 30)
    @Schema(description = "报销分类")
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "报销金额")
    private BigDecimal amount;

    @Column(name = "voucher_url", length = 500)
    @Schema(description = "凭证图片URL")
    private String voucherUrl;

    /**
     * 状态：PENDING-待审批，APPROVED-已批准，REJECTED-已拒绝
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "状态")
    private String status = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    @Schema(description = "审批人")
    private Employee approver;

    @Column(name = "approve_time")
    @Schema(description = "审批时间")
    private LocalDateTime approveTime;

    @Column(name = "approve_remark", length = 500)
    @Schema(description = "审批备注")
    private String approveRemark;

    @Column(name = "create_time")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
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
