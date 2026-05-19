package com.petclinic.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 智能设备实体类
 * 用于管理智能喂食器/饮水机等设备信息
 * 
 * @author Pet Clinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "smart_device")
@Schema(description = "智能设备")
public class SmartDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "设备ID")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "设备名称", required = true)
    private String deviceName;

    @Column(nullable = false, length = 50)
    @Schema(description = "设备编号", required = true)
    private String deviceCode;

    @Column(length = 50)
    @Schema(description = "设备类型：FEEDER-喂食器, WATER_DISPENSER-饮水机, OTHER-其他")
    private String deviceType;

    @Column(length = 100)
    @Schema(description = "设备型号")
    private String deviceModel;

    @Column(length = 200)
    @Schema(description = "关联宠物ID")
    private String petId;

    @Column(length = 100)
    @Schema(description = "关联宠物名称")
    private String petName;

    @Column(length = 50)
    @Schema(description = "设备状态：ONLINE-在线, OFFLINE-离线, ERROR-故障")
    private String status;

    @Column(length = 500)
    @Schema(description = "设备位置")
    private String location;

    @Column(length = 200)
    @Schema(description = "最后在线时间")
    private String lastOnlineTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "OFFLINE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
