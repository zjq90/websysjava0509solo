package com.production.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 工艺参数实体类
 * 用于记录各工序的生产工艺参数，支持工艺参数的留存和追溯
 */
@Entity
@Table(name = "process_parameters")
public class ProcessParameter {

    /**
     * 参数ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的加工记录ID
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id", nullable = false)
    private ProcessingRecord processingRecord;

    /**
     * 参数名称，如：温度、转速、湿度、压力等
     */
    @Column(nullable = false, length = 100)
    private String parameterName;

    /**
     * 参数代码
     */
    @Column(length = 50)
    private String parameterCode;

    /**
     * 参数值
     */
    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal parameterValue;

    /**
     * 参数单位，如：℃、rpm、%、MPa等
     */
    @Column(length = 20)
    private String parameterUnit;

    /**
     * 标准值上限
     */
    @Column(precision = 15, scale = 4)
    private BigDecimal upperLimit;

    /**
     * 标准值下限
     */
    @Column(precision = 15, scale = 4)
    private BigDecimal lowerLimit;

    /**
     * 是否在标准范围内：NORMAL(正常), ABNORMAL(异常)
     */
    @Column(length = 20)
    private String valueStatus;

    /**
     * 记录时间
     */
    @Column(nullable = false)
    private LocalDateTime recordTime;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String remarks;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    // ==================== Getters and Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProcessingRecord getProcessingRecord() {
        return processingRecord;
    }

    public void setProcessingRecord(ProcessingRecord processingRecord) {
        this.processingRecord = processingRecord;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterCode() {
        return parameterCode;
    }

    public void setParameterCode(String parameterCode) {
        this.parameterCode = parameterCode;
    }

    public BigDecimal getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(BigDecimal parameterValue) {
        this.parameterValue = parameterValue;
    }

    public String getParameterUnit() {
        return parameterUnit;
    }

    public void setParameterUnit(String parameterUnit) {
        this.parameterUnit = parameterUnit;
    }

    public BigDecimal getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public String getValueStatus() {
        return valueStatus;
    }

    public void setValueStatus(String valueStatus) {
        this.valueStatus = valueStatus;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.recordTime == null) {
            this.recordTime = LocalDateTime.now();
        }
        checkValueStatus();
    }

    /**
     * 检查参数值是否在标准范围内
     */
    public void checkValueStatus() {
        if (this.parameterValue == null) {
            return;
        }
        boolean normal = true;
        if (this.upperLimit != null && this.parameterValue.compareTo(this.upperLimit) > 0) {
            normal = false;
        }
        if (this.lowerLimit != null && this.parameterValue.compareTo(this.lowerLimit) < 0) {
            normal = false;
        }
        this.valueStatus = normal ? "NORMAL" : "ABNORMAL";
    }
}