package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 报销审批记录表实体类
 * 记录报销审批的完整流程，实现审批留痕功能
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "ps_reimbursement_approval_record")
@Schema(description = "报销审批记录")
public class ReimbursementApprovalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reimbursement_id", nullable = false)
    @Schema(description = "关联报销ID")
    private Reimbursement reimbursement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id", nullable = false)
    @Schema(description = "操作人")
    private Employee operator;

    /**
     * 操作类型：APPLY-申请，APPROVE-批准，REJECT-拒绝，REAPPLY-重新申请
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "操作类型")
    private String operationType;

    @Column(name = "previous_status", length = 20)
    @Schema(description = "操作前状态")
    private String previousStatus;

    @Column(name = "current_status", length = 20)
    @Schema(description = "操作后状态")
    private String currentStatus;

    @Column(length = 500)
    @Schema(description = "操作备注")
    private String remark;

    @Column(name = "create_time")
    @Schema(description = "操作时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
