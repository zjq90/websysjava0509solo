package com.culturalrelic.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * 物联网设备实体类
 * 支持温湿度传感器，NFC近场通信绑定
 */
@Data
@Entity
@Table(name = "iot_device")
@EqualsAndHashCode(callSuper = true)
public class IotDevice extends BaseEntity {

    /**
     * 设备编号
     */
    @Column(name = "device_no", nullable = false, unique = true, length = 100)
    private String deviceNo;

    /**
     * 设备名称
     */
    @Column(name = "device_name", nullable = false, length = 200)
    private String deviceName;

    /**
     * 设备类型：1-温湿度传感器，2-温度传感器，3-湿度传感器，4-其他
     */
    @Column(name = "device_type", nullable = false)
    private Integer deviceType;

    /**
     * NFC标签ID（用于NFC近场通信绑定）
     */
    @Column(name = "nfc_tag_id", unique = true, length = 200)
    private String nfcTagId;

    /**
     * 绑定的文物ID
     */
    @Column(name = "relic_id")
    private Long relicId;

    /**
     * 绑定的文物名称
     */
    @Column(name = "relic_name", length = 200)
    private String relicName;

    /**
     * 设备状态：0-离线，1-在线，2-故障，3-维护中
     */
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    /**
     * 当前温度（摄氏度）
     */
    @Column(name = "current_temperature")
    private Double currentTemperature;

    /**
     * 当前湿度（百分比）
     */
    @Column(name = "current_humidity")
    private Double currentHumidity;

    /**
     * 最低温度阈值
     */
    @Column(name = "min_temperature")
    private Double minTemperature = 10.0;

    /**
     * 最高温度阈值
     */
    @Column(name = "max_temperature")
    private Double maxTemperature = 30.0;

    /**
     * 最低湿度阈值
     */
    @Column(name = "min_humidity")
    private Double minHumidity = 30.0;

    /**
     * 最高湿度阈值
     */
    @Column(name = "max_humidity")
    private Double maxHumidity = 70.0;

    /**
     * 是否告警：0-正常，1-告警
     */
    @Column(name = "alarm_status", nullable = false)
    private Integer alarmStatus = 0;

    /**
     * 告警信息
     */
    @Column(name = "alarm_message", length = 500)
    private String alarmMessage;

    /**
     * 最后数据上报时间
     */
    @Column(name = "last_report_time")
    private String lastReportTime;

    /**
     * 安装位置
     */
    @Column(name = "install_location", length = 200)
    private String installLocation;

    /**
     * 设备厂商
     */
    @Column(name = "manufacturer", length = 200)
    private String manufacturer;

    /**
     * 设备型号
     */
    @Column(name = "model", length = 100)
    private String model;

    /**
     * 固件版本
     */
    @Column(name = "firmware_version", length = 50)
    private String firmwareVersion;

    /**
     * 是否绑定：0-未绑定，1-已绑定
     */
    @Column(name = "bound", nullable = false)
    private Integer bound = 0;

    /**
     * 绑定时间
     */
    @Column(name = "bind_time")
    private String bindTime;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
}
