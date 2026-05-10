package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 设备实体类
 * 售卖设备的基本信息，包括设备编号、位置信息、型号等
 */
@Entity
@Table(name = "biz_device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "设备")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "设备ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "设备编号")
    private String deviceCode;

    @Column(length = 100)
    @Schema(description = "设备名称")
    private String deviceName;

    @Column(length = 50)
    @Schema(description = "设备型号")
    private String model;

    @Column(length = 100)
    @Schema(description = "固件版本")
    private String firmwareVersion;

    @Column(nullable = false, length = 200)
    @Schema(description = "投放位置")
    private String location;

    @Column(precision = 10, scale = 6)
    @Schema(description = "纬度")
    private BigDecimal latitude;

    @Column(precision = 10, scale = 6)
    @Schema(description = "经度")
    private BigDecimal longitude;

    @Column(length = 100)
    @Schema(description = "负责人")
    private String manager;

    @Column(length = 20)
    @Schema(description = "联系电话")
    private String contactPhone;

    @Column(nullable = false)
    @Schema(description = "设备状态：1在线，2离线，3故障")
    private Integer status = 1;

    @Column(name = "install_time")
    @Schema(description = "安装时间")
    private LocalDateTime installTime;

    @Column(name = "last_report_time")
    @Schema(description = "最后上报时间")
    private LocalDateTime lastReportTime;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
