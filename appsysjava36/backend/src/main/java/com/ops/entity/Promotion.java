package com.ops.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 营销活动实体类
 * 存储各种营销活动的配置信息
 * 
 * @author ops-admin
 */
@Data
@Entity
@Table(name = "biz_promotion")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "discount_rate", precision = 5, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "package_ids", length = 500)
    private String packageIds;

    @Column(name = "target_user_type", length = 50)
    private String targetUserType;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "participant_count")
    private Integer participantCount;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        participantCount = 0;
        status = "DRAFT";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
