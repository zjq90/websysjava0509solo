package com.appsys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wifi_setting")
public class WiFiSetting extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "ssid", nullable = false, length = 50)
    private String ssid;

    @Column(name = "password", length = 50)
    private String password;

    @Column(name = "is_hidden", nullable = false)
    private Boolean isHidden = false;

    @Column(name = "band", length = 20)
    private String band = "2.4G";

    @Column(name = "channel")
    private Integer channel;

    @Column(name = "guest_ssid", length = 50)
    private String guestSsid;

    @Column(name = "guest_password", length = 50)
    private String guestPassword;

    @Column(name = "guest_enabled", nullable = false)
    private Boolean guestEnabled = false;

    @Column(name = "status", length = 20)
    private String status = "ENABLED";
}
