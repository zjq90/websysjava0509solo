package com.inventory.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 温湿度监控实体类
 * 功能：记录仓库/门店的温湿度数据，支持温湿度预警
 */
@Entity
@Table(name = "environment_monitor")
public class EnvironmentMonitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull(message = "温度不能为空")
    @Min(value = -50, message = "温度不能低于-50°C")
    @Max(value = 100, message = "温度不能超过100°C")
    private BigDecimal temperature;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull(message = "湿度不能为空")
    @Min(value = 0, message = "湿度不能低于0%")
    @Max(value = 100, message = "湿度不能超过100%")
    private BigDecimal humidity;

    @Column(length = 50)
    @Size(max = 50, message = "设备编码长度不能超过50")
    private String deviceCode;

    private LocalDateTime monitorTime;

    @PrePersist
    protected void onCreate() {
        if (monitorTime == null) {
            monitorTime = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Warehouse getWarehouse() { return warehouse; }
    public void setWarehouse(Warehouse warehouse) { this.warehouse = warehouse; }
    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
    public java.math.BigDecimal getTemperature() { return temperature; }
    public void setTemperature(java.math.BigDecimal temperature) { this.temperature = temperature; }
    public java.math.BigDecimal getHumidity() { return humidity; }
    public void setHumidity(java.math.BigDecimal humidity) { this.humidity = humidity; }
    public String getDeviceCode() { return deviceCode; }
    public void setDeviceCode(String deviceCode) { this.deviceCode = deviceCode; }
    public LocalDateTime getMonitorTime() { return monitorTime; }
    public void setMonitorTime(LocalDateTime monitorTime) { this.monitorTime = monitorTime; }
}
