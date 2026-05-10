package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备状态实体类
 * 实时监控设备的运行状态、网络质量、信号强度、货道电机、门锁、温湿度、电力状况等
 */
@Entity
@Table(name = "biz_device_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "设备状态")
public class DeviceStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "记录ID")
    private Long id;

    @Column(name = "device_id", nullable = false)
    @Schema(description = "设备ID")
    private Long deviceId;

    @Column(name = "device_code", nullable = false, length = 50)
    @Schema(description = "设备编号")
    private String deviceCode;

    @Column(name = "network_quality")
    @Schema(description = "网络质量：1优秀，2良好，3一般，4较差")
    private Integer networkQuality = 2;

    @Column(name = "signal_strength")
    @Schema(description = "信号强度（dBm）")
    private Integer signalStrength = -65;

    @Column(name = "motor_status")
    @Schema(description = "货道电机状态：1正常，2异常")
    private Integer motorStatus = 1;

    @Column(name = "door_status")
    @Schema(description = "门锁状态：1已锁，2已开，3异常")
    private Integer doorStatus = 1;

    @Column(name = "temperature")
    @Schema(description = "温度（摄氏度）")
    private BigDecimal temperature = new BigDecimal("4.5");

    @Column(name = "humidity")
    @Schema(description = "湿度（百分比）")
    private BigDecimal humidity = new BigDecimal("65.0");

    @Column(name = "power_status")
    @Schema(description = "电力状况：1正常，2低电量，3断电")
    private Integer powerStatus = 1;

    @Column(name = "battery_level")
    @Schema(description = "电池电量（百分比）")
    private Integer batteryLevel = 100;

    @Column(name = "target_temperature")
    @Schema(description = "目标温度（摄氏度）")
    private BigDecimal targetTemperature = new BigDecimal("4.0");

    @Column(name = "advert_content", length = 500)
    @Schema(description = "屏幕广告内容")
    private String advertContent;

    @Column(name = "report_time", nullable = false)
    @Schema(description = "上报时间")
    private LocalDateTime reportTime = LocalDateTime.now();

    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime = LocalDateTime.now();
}
