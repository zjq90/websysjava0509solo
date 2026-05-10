package com.management.platform.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备故障记录实体类
 * 存储设备故障和维修记录
 */
@Entity
@Table(name = "device_faults")
public class DeviceFault {

    /**
     * 故障记录ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备ID
     */
    @Column(nullable = false)
    private Long deviceId;

    /**
     * 故障描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 故障等级：low（低）, medium（中）, high（高）
     */
    @Column(length = 20)
    private String severity = "medium";

    /**
     * 维修费用
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal repairCost = BigDecimal.ZERO;

    /**
     * 维修时长（小时）
     */
    @Column(nullable = false)
    private Double repairDuration = 0.0;

    /**
     * 故障发生时间
     */
    @Column(nullable = false)
    private LocalDateTime faultTime;

    /**
     * 维修完成时间
     */
    private LocalDateTime repairTime;

    /**
     * 状态：reported（已上报）, repairing（维修中）, fixed（已修复）
     */
    @Column(length = 20)
    private String status = "fixed";

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (faultTime == null) {
            faultTime = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public BigDecimal getRepairCost() { return repairCost; }
    public void setRepairCost(BigDecimal repairCost) { this.repairCost = repairCost; }
    public Double getRepairDuration() { return repairDuration; }
    public void setRepairDuration(Double repairDuration) { this.repairDuration = repairDuration; }
    public LocalDateTime getFaultTime() { return faultTime; }
    public void setFaultTime(LocalDateTime faultTime) { this.faultTime = faultTime; }
    public LocalDateTime getRepairTime() { return repairTime; }
    public void setRepairTime(LocalDateTime repairTime) { this.repairTime = repairTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
