package com.production.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备生产记录实体类
 * 用于存储从设备系统传来的实时生产数据
 */
@Entity
@Table(name = "equipment_records")
public class EquipmentRecord {

    /**
     * 记录ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的设备ID
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    /**
     * 关联的生产计划ID（可选）
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id")
    private ProductionPlan productionPlan;

    /**
     * 关联的加工记录ID（可选）
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id")
    private ProcessingRecord processingRecord;

    /**
     * 数据来源：AUTO(自动采集), MANUAL(手动录入)
     */
    @Column(nullable = false, length = 20)
    private String dataSource;

    /**
     * 运行状态：OFFLINE(离线), IDLE(空闲), RUNNING(运行中), PAUSED(暂停), FAULT(故障)
     */
    @Column(nullable = false, length = 20)
    private String runStatus;

    /**
     * 当前速度/产量（单位/分钟）
     */
    @Column(precision = 15, scale = 4)
    private BigDecimal currentSpeed;

    /**
     * 本次运行产量
     */
    @Column(precision = 15, scale = 4)
    private BigDecimal productionOutput;

    /**
     * 运行时长（分钟）
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal runDuration;

    /**
     * 设备温度
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal temperature;

    /**
     * 设备压力
     */
    @Column(precision = 10, scale = 2)
    private BigDecimal pressure;

    /**
     * 告警信息
     */
    @Column(columnDefinition = "TEXT")
    private String alarmMessage;

    /**
     * 原始数据包（JSON格式）
     */
    @Column(columnDefinition = "TEXT")
    private String rawData;

    /**
     * 数据采集时间
     */
    @Column(nullable = false)
    private LocalDateTime recordTime;

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

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public ProductionPlan getProductionPlan() {
        return productionPlan;
    }

    public void setProductionPlan(ProductionPlan productionPlan) {
        this.productionPlan = productionPlan;
    }

    public ProcessingRecord getProcessingRecord() {
        return processingRecord;
    }

    public void setProcessingRecord(ProcessingRecord processingRecord) {
        this.processingRecord = processingRecord;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(String runStatus) {
        this.runStatus = runStatus;
    }

    public BigDecimal getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(BigDecimal currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public BigDecimal getProductionOutput() {
        return productionOutput;
    }

    public void setProductionOutput(BigDecimal productionOutput) {
        this.productionOutput = productionOutput;
    }

    public BigDecimal getRunDuration() {
        return runDuration;
    }

    public void setRunDuration(BigDecimal runDuration) {
        this.runDuration = runDuration;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getPressure() {
        return pressure;
    }

    public void setPressure(BigDecimal pressure) {
        this.pressure = pressure;
    }

    public String getAlarmMessage() {
        return alarmMessage;
    }

    public void setAlarmMessage(String alarmMessage) {
        this.alarmMessage = alarmMessage;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public LocalDateTime getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(LocalDateTime recordTime) {
        this.recordTime = recordTime;
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
    }
}