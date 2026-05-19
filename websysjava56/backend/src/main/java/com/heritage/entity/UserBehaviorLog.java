package com.heritage.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_behavior_log")
public class UserBehaviorLog extends BaseEntity {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "behavior_type", length = 50)
    private String behaviorType;

    @Column(name = "target_type", length = 50)
    private String targetType;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "target_name", length = 200)
    private String targetName;

    @Column(name = "page_url", length = 500)
    private String pageUrl;

    @Column(name = "referrer_url", length = 500)
    private String referrerUrl;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "stay_duration")
    private Long stayDuration;

    @Column(name = "click_count")
    private Integer clickCount;

    @Column(name = "scroll_depth")
    private Integer scrollDepth;

    @Column(name = "behavior_time", nullable = false)
    private LocalDateTime behaviorTime;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "extra_data", columnDefinition = "TEXT")
    private String extraData;
}
