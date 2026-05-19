package com.pethospital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 预约实体类
 * 存储用户的预约信息，支持预约改签/取消和双重提醒
 * 
 * @author Pet Hospital Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long petId;

    @Column(nullable = false)
    private Long doctorId;

    private Long scheduleId;

    @Column(length = 50)
    private String petName;

    @Column(length = 50)
    private String doctorName;

    @Column(length = 50)
    private String department;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(length = 500)
    private String symptoms;

    @Column(length = 20)
    private String status = "PENDING";

    private Boolean smsReminderSent = false;

    private Boolean appReminderSent = false;

    @Column(length = 500)
    private String cancelReason;

    private LocalDateTime cancelTime;

    @Column(updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
