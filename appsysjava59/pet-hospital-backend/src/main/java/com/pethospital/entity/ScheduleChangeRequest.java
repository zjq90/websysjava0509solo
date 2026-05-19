package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "schedule_change_request")
public class ScheduleChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "original_schedule_id")
    private Long originalScheduleId;

    @Column(name = "request_type", length = 20)
    private String requestType;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "target_shift", length = 20)
    private String targetShift;

    @Column(length = 500)
    private String reason;

    @Column(length = 20)
    private String status;

    @Column(name = "approver_id")
    private Long approverId;

    @Column(name = "approve_time")
    private LocalDateTime approveTime;

    @Column(name = "approve_notes", length = 500)
    private String approveNotes;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
