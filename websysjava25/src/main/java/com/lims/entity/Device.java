package com.lims.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 设备实体类
 * 存储检验/检查设备信息，用于设备接口对接
 *
 * @author LIMS Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")
@Schema(description = "设备信息")
public class Device {

    /**
     * 设备ID，主键自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "设备ID", example = "1")
    private Long id;

    /**
     * 设备编号，唯一标识
     */
    @Column(unique = true, nullable = false, length = 50)
    @Schema(description = "设备编号", example = "DEV001")
    private String deviceCode;

    /**
     * 设备名称
     */
    @Column(nullable = false, length = 100)
    @Schema(description = "设备名称", example = "全自动生化分析仪")
    private String deviceName;

    /**
     * 设备类型：LABORATORY-检验设备，EXAMINATION-检查设备
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "设备类型", example = "LABORATORY")
    private String deviceType;

    /**
     * 设备型号
     */
    @Column(length = 100)
    @Schema(description = "设备型号", example = "BS-800")
    private String deviceModel;

    /**
     * 生产厂家
     */
    @Column(length = 100)
    @Schema(description = "生产厂家", example = "迈瑞医疗")
    private String manufacturer;

    /**
     * 所属科室ID
     */
    @Column
    @Schema(description = "所属科室ID", example = "1")
    private Long departmentId;

    /**
     * 接口协议类型：HL7，DICOM，CUSTOM，NONE
     */
    @Column(length = 50)
    @Schema(description = "接口协议", example = "HL7")
    private String interfaceProtocol;

    /**
     * 接口地址
     */
    @Column(length = 200)
    @Schema(description = "接口地址", example = "192.168.1.100:8080")
    private String interfaceAddress;

    /**
     * 接口配置参数（JSON格式）
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "接口配置", example = "{\"timeout\": 30}")
    private String interfaceConfig;

    /**
     * 设备状态：ONLINE-在线，OFFLINE-离线，MAINTENANCE-维护中
     */
    @Column(nullable = false, length = 50)
    @Schema(description = "设备状态", example = "ONLINE")
    private String status;

    /**
     * 最后通信时间
     */
    @Column
    @Schema(description = "最后通信时间")
    private LocalDateTime lastCommunicationTime;

    /**
     * 设备描述
     */
    @Column(length = 500)
    @Schema(description = "设备描述", example = "用于生化检验")
    private String description;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        status = "OFFLINE";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
