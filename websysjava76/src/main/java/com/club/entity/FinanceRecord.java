package com.club.entity;

import com.club.enums.FinanceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 财务记录实体类
 * 存储社团经费流水记录
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "finance_record")
@Schema(description = "财务记录")
public class FinanceRecord extends BaseEntity {

    /**
     * 关联社团ID
     */
    @Column(name = "club_id", nullable = false)
    @Schema(description = "社团ID", example = "1")
    private Long clubId;

    /**
     * 社团名称
     */
    @Column(name = "club_name", length = 100)
    @Schema(description = "社团名称", example = "计算机协会")
    private String clubName;

    /**
     * 财务类型（收入/支出/报销）
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    @Schema(description = "财务类型", example = "EXPENSE")
    private FinanceType type;

    /**
     * 记录编号
     */
    @Column(name = "record_no", unique = true, length = 50)
    @Schema(description = "记录编号", example = "FIN202405001")
    private String recordNo;

    /**
     * 金额
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    @Schema(description = "金额", example = "500.00")
    private BigDecimal amount;

    /**
     * 收支类别
     */
    @Column(name = "category", length = 50)
    @Schema(description = "收支类别", example = "活动经费")
    private String category;

    /**
     * 用途描述
     */
    @Column(name = "purpose", nullable = false, length = 500)
    @Schema(description = "用途描述", example = "购买活动奖品")
    private String purpose;

    /**
     * 发生日期
     */
    @Column(name = "occur_date")
    @Schema(description = "发生日期", example = "2024-05-20")
    private LocalDate occurDate;

    /**
     * 经办人
     */
    @Column(name = "handler", length = 50)
    @Schema(description = "经办人", example = "赵六")
    private String handler;

    /**
     * 凭证号
     */
    @Column(name = "voucher_no", length = 50)
    @Schema(description = "凭证号", example = "PZ202405001")
    private String voucherNo;

    /**
     * 凭证URL
     */
    @Column(name = "voucher_url", length = 500)
    @Schema(description = "凭证URL", example = "http://example.com/voucher.jpg")
    private String voucherUrl;

    /**
     * 是否有凭证
     */
    @Column(name = "has_voucher")
    @Schema(description = "是否有凭证", example = "true")
    private Boolean hasVoucher = true;

    /**
     * 是否为异常支出
     */
    @Column(name = "abnormal")
    @Schema(description = "是否异常", example = "false")
    private Boolean abnormal = false;

    /**
     * 异常说明
     */
    @Column(name = "abnormal_note", length = 500)
    @Schema(description = "异常说明")
    private String abnormalNote;

    /**
     * 是否已标记调查
     */
    @Column(name = "marked_for_investigation")
    @Schema(description = "是否标记调查", example = "false")
    private Boolean markedForInvestigation = false;

    /**
     * 调查状态（0-未调查，1-调查中，2-已完成）
     */
    @Column(name = "investigation_status")
    @Schema(description = "调查状态", example = "0")
    private Integer investigationStatus = 0;

    /**
     * 调查结果
     */
    @Column(name = "investigation_result", length = 1000)
    @Schema(description = "调查结果")
    private String investigationResult;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;
}
