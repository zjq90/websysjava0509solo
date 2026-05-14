package com.appsys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "child_guard")
public class ChildGuard extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "child_name", length = 50)
    private String childName;

    @Column(name = "device_id", length = 50)
    private String deviceId;

    @Column(name = "start_time", length = 10)
    private String startTime;

    @Column(name = "end_time", length = 10)
    private String endTime;

    @Column(name = "week_days", length = 20)
    private String weekDays;

    @Column(name = "app_whitelist", length = 1000)
    private String appWhitelist;

    @Column(name = "anti_addiction", nullable = false)
    private Boolean antiAddiction = true;

    @Column(name = "reminder_time")
    private Integer reminderTime = 60;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;
}
