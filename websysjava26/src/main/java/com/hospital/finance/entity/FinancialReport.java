package com.hospital.finance.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 财务报表实体类
 * 记录日报、月报等财务对账数据
 */
@Data
@Entity
@Table(name = "financial_report")
@Schema(description = "财务报表")
public class FinancialReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "报表ID")
    private Long id;

    @Column(name = "report_no", unique = true, nullable = false, length = 50)
    @Schema(description = "报表编号")
    private String reportNo;

    @Column(name = "report_type", length = 20)
    @Schema(description = "报表类型(日报、月报、年报)")
    private String reportType;

    @Column(name = "report_date")
    @Schema(description = "报表日期")
    private LocalDate reportDate;

    @Column(name = "outpatient_count")
    @Schema(description = "门诊收费笔数")
    private Integer outpatientCount;

    @Column(name = "outpatient_amount", precision = 10, scale = 2)
    @Schema(description = "门诊收费金额")
    private BigDecimal outpatientAmount;

    @Column(name = "inpatient_count")
    @Schema(description = "住院收费笔数")
    private Integer inpatientCount;

    @Column(name = "inpatient_amount", precision = 10, scale = 2)
    @Schema(description = "住院收费金额")
    private BigDecimal inpatientAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    @Schema(description = "总收费金额")
    private BigDecimal totalAmount;

    @Column(name = "insurance_amount", precision = 10, scale = 2)
    @Schema(description = "医保总金额")
    private BigDecimal insuranceAmount;

    @Column(name = "self_pay_amount", precision = 10, scale = 2)
    @Schema(description = "自付总金额")
    private BigDecimal selfPayAmount;

    @Column(name = "refund_count")
    @Schema(description = "退费笔数")
    private Integer refundCount;

    @Column(name = "refund_amount", precision = 10, scale = 2)
    @Schema(description = "退费金额")
    private BigDecimal refundAmount;

    @Column(name = "cash_amount", precision = 10, scale = 2)
    @Schema(description = "现金支付金额")
    private BigDecimal cashAmount;

    @Column(name = "wechat_amount", precision = 10, scale = 2)
    @Schema(description = "微信支付金额")
    private BigDecimal wechatAmount;

    @Column(name = "alipay_amount", precision = 10, scale = 2)
    @Schema(description = "支付宝支付金额")
    private BigDecimal alipayAmount;

    @Column(name = "bank_card_amount", precision = 10, scale = 2)
    @Schema(description = "银行卡支付金额")
    private BigDecimal bankCardAmount;

    @Column(name = "status", length = 20)
    @Schema(description = "状态(待审核、已审核、已结账)")
    private String status;

    @Column(name = "operator", length = 100)
    @Schema(description = "操作员")
    private String operator;

    @Column(name = "auditor", length = 100)
    @Schema(description = "审核员")
    private String auditor;

    @Column(name = "audit_time")
    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Column(name = "remark", length = 500)
    @Schema(description = "备注")
    private String remark;

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