package com.appsys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "network_device")
public class NetworkDevice extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "device_id", nullable = false, unique = true, length = 50)
    private String deviceId;

    @Column(name = "device_name", length = 50)
    private String deviceName;

    @Column(name = "device_type", length = 30)
    private String deviceType;

    @Column(name = "mac_address", length = 20)
    private String macAddress;

    @Column(name = "ip_address", length = 20)
    private String ipAddress;

    @Column(name = "signal_strength")
    private Integer signalStrength;

    @Column(name = "status", length = 20)
    private String status = "ONLINE";

    @Column(name = "upload_speed")
    private Double uploadSpeed;

    @Column(name = "download_speed")
    private Double downloadSpeed;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "speed_limit")
    private Integer speedLimit;

    @Column(name = "position", length = 50)
    private String position;

    @Column(name = "parent_device_id", length = 50)
    private String parentDeviceId;
}
