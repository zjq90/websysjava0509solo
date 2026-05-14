package com.ops.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 工单实体类
 * 存储工单的完整信息，支持全流程跟踪
 * 
 * @author ops-admin
 */
@Data
@Entity
@Table(name = "biz_work_order")
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo;

    @Column(name = "order_type", length = 50)
    private String orderType;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", length = 50)
    private String userName;

    @Column(name = "user_phone", length = 20)
    private String userPhone;

    @Column(name = "user_address", length = 255)
    private String userAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "technician_id")
    private Long technicianId;

    @Column(name = "technician_name", length = 50)
    private String technicianName;

    @Column(name = "technician_phone", length = 20)
    private String technicianPhone;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "photo_urls", length = 1000)
    private String photoUrls;

    @Column(name = "signature_url", length = 255)
    private String signatureUrl;

    @Column(name = "satisfaction_score")
    private Integer satisfactionScore;

    @Column(name = "evaluation", length = 500)
    private String evaluation;

    @Column(name = "assign_time")
    private LocalDateTime assignTime;

    @Column(name = "accept_time")
    private LocalDateTime acceptTime;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    @Column(name = "response_duration")
    private Long responseDuration;

    @Column(name = "handle_duration")
    private Long handleDuration;

    @Column(name = "is_first_fix")
    private Boolean isFirstFix;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        status = "PENDING";
        isFirstFix = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
