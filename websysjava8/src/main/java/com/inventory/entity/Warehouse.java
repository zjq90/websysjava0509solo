package com.inventory.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

/**
 * 仓库实体类
 * 用于管理仓库信息，支持多仓库管理
 * 每个仓库可以存储多个批次的种子库存
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "warehouse")
public class Warehouse extends BaseEntity {

    /**
     * 仓库名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 仓库编码，唯一标识
     */
    @Column(name = "code", unique = true, length = 50)
    private String code;

    /**
     * 仓库地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 仓库负责人
     */
    @Column(name = "manager", length = 50)
    private String manager;

    /**
     * 联系电话
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 仓库面积（平方米）
     */
    @Column(name = "area")
    private Double area;

    /**
     * 仓库类型：
     * 1-常温库
     * 2-冷藏库
     * 3-恒温恒湿库
     */
    @Column(name = "type", nullable = false)
    private Integer type = 1;

    /**
     * 当前温度（可实时更新）
     */
    @Column(name = "current_temperature")
    private Double currentTemperature;

    /**
     * 当前湿度（可实时更新）
     */
    @Column(name = "current_humidity")
    private Double currentHumidity;

    /**
     * 温度预警阈值最小值
     */
    @Column(name = "temp_warning_min")
    private Double tempWarningMin;

    /**
     * 温度预警阈值最大值
     */
    @Column(name = "temp_warning_max")
    private Double tempWarningMax;

    /**
     * 湿度预警阈值最小值
     */
    @Column(name = "humidity_warning_min")
    private Double humidityWarningMin;

    /**
     * 湿度预警阈值最大值
     */
    @Column(name = "humidity_warning_max")
    private Double humidityWarningMax;

    /**
     * 状态：1-启用，0-禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
