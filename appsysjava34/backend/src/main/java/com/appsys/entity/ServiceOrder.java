package com.appsys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "service_order")
public class ServiceOrder extends BaseEntity {

    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "service_type", nullable = false, length = 50)
    private String serviceType;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "current_step", nullable = false)
    private Integer currentStep = 1;

    @Column(name = "total_steps", nullable = false)
    private Integer totalSteps = 4;

    @Column(name = "worker_id")
    private Long workerId;

    @Column(name = "worker_name", length = 50)
    private String workerName;

    @Column(name = "worker_phone", length = 20)
    private String workerPhone;

    @Column(name = "worker_lat")
    private Double workerLat;

    @Column(name = "worker_lng")
    private Double workerLng;

    @Column(name = "eta_time")
    private LocalDateTime etaTime;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "remark", length = 500)
    private String remark;
}
