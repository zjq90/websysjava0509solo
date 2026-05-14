package com.appsys.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "fault_report")
public class FaultReport extends BaseEntity {

    @Column(name = "report_no", nullable = false, unique = true, length = 50)
    private String reportNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "fault_type", nullable = false, length = 50)
    private String faultType;

    @Column(name = "fault_desc", length = 500)
    private String faultDesc;

    @Column(name = "images", length = 1000)
    private String images;

    @Column(name = "video", length = 255)
    private String video;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "PENDING";

    @Column(name = "diagnosis_result", length = 1000)
    private String diagnosisResult;

    @Column(name = "solution", length = 1000)
    private String solution;

    @Column(name = "worker_id")
    private Long workerId;

    @Column(name = "worker_name", length = 50)
    private String workerName;

    @Column(name = "worker_phone", length = 20)
    private String workerPhone;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;
}
