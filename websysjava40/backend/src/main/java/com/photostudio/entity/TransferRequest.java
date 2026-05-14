package com.photostudio.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 调班申请实体类
 * 用于管理员工调班申请与审批流程
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "transfer_request")
@Schema(description = "调班申请信息")
public class TransferRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "申请ID", example = "1")
    private Long id;

    @NotNull(message = "原排班不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_schedule_id", nullable = false)
    @Schema(description = "原排班信息")
    private Schedule originalSchedule;

    @NotNull(message = "申请人不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    @Schema(description = "申请人")
    private Employee requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_employee_id")
    @Schema(description = "目标调班员工")
    private Employee targetEmployee;

    @NotNull(message = "申请原因不能为空")
    @Column(nullable = false, length = 500)
    @Schema(description = "申请原因", example = "个人事务需要处理")
    private String reason;

    @Column(nullable = false, length = 20)
    @Schema(description = "申请状态", example = "PENDING")
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    @Schema(description = "审批人")
    private Employee approver;

    @Column(length = 500)
    @Schema(description = "审批意见")
    private String approvalRemark;

    @Column
    @Schema(description = "审批时间")
    private LocalDateTime approvalTime;

    @Column
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column
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

    /**
     * 申请状态枚举
     */
    public enum RequestStatus {
        PENDING("待审批"),
        APPROVED("已通过"),
        REJECTED("已拒绝"),
        CANCELLED("已取消");

        private final String description;

        RequestStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
