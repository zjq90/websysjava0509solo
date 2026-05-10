package com.management.platform.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备实体类
 * 存储设备的基本信息和效能数据
 */
@Entity
@Table(name = "devices")
public class Device {

    /**
     * 设备ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备编号
     */
    @Column(nullable = false, unique = true, length = 50)
    private String deviceCode;

    /**
     * 设备名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 设备类型
     */
    @Column(length = 50)
    private String type;

    /**
     * 安装位置
     */
    @Column(length = 100)
    private String location;

    /**
     * 设备状态：running（运行中）, maintenance（维护中）, stopped（停止）
     */
    @Column(length = 20)
    private String status = "running";

    /**
     * 总运行时长（小时）
     */
    @Column(nullable = false)
    private Double totalRuntime = 0.0;

    /**
     * 总产出数量
     */
    @Column(nullable = false)
    private Long totalOutput = 0L;

    /**
     * 故障次数
     */
    @Column(nullable = false)
    private Integer faultCount = 0;

    /**
     * 累计运维成本
     */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal maintenanceCost = BigDecimal.ZERO;

    /**
     * 安装日期
     */
    private LocalDateTime installationDate;

    /**
     * 创建时间
     */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (installationDate == null) {
            installationDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceCode() { return deviceCode; }
    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Double getTotalRuntime() { return totalRuntime; }
    public void setTotalRuntime(Double totalRuntime) { this.totalRuntime = totalRuntime; }
    public Long getTotalOutput() { return totalOutput; }
    public void setTotalOutput(Long totalOutput) { this.totalOutput = totalOutput; }
    public Integer getFaultCount() { return faultCount; }
    public void setFaultCount(Integer faultCount) { this.faultCount = faultCount; }
    public BigDecimal getMaintenanceCost() { return maintenanceCost; }
    public void setMaintenanceCost(BigDecimal maintenanceCost) { this.maintenanceCost = maintenanceCost; }
    public LocalDateTime getInstallationDate() { return installationDate; }
    public void setInstallationDate(LocalDateTime installationDate) { this.installationDate = installationDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
