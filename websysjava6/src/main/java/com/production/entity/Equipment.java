package com.production.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 设备实体类
 * 用于管理生产系统中的设备信息，如自动化包装线等
 */
@Entity
@Table(name = "equipment")
public class Equipment {

    /**
     * 设备ID，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 设备编号，唯一标识
     */
    @Column(nullable = false, unique = true, length = 50)
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Column(nullable = false, length = 100)
    private String equipmentName;

    /**
     * 设备类型：CLEANER(清选机), COATER(包衣机), PACKAGER(包装机), INSPECTOR(检测仪), CONVEYOR(输送机)
     */
    @Column(length = 50)
    private String equipmentType;

    /**
     * 设备型号
     */
    @Column(length = 100)
    private String model;

    /**
     * 制造商
     */
    @Column(length = 100)
    private String manufacturer;

    /**
     * 安装位置/车间
     */
    @Column(length = 100)
    private String location;

    /**
     * IP地址（用于设备通信）
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 设备状态：OFFLINE(离线), IDLE(空闲), RUNNING(运行中), MAINTENANCE(维护中), FAULT(故障)
     */
    @Column(nullable = false, length = 20)
    private String status;

    /**
     * 负责人
     */
    @Column(length = 50)
    private String responsiblePerson;

    /**
     * 设备描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    // ==================== Getters and Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(String responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = "OFFLINE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}