package com.pethospital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 用药提醒实体类
 * 存储用药提醒信息，支持多设备同步
 * 
 * @author Pet Hospital Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medication_reminders")
public class MedicationReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long petId;

    @Column(length = 50)
    private String petName;

    @Column(nullable = false, length = 200)
    private String medicineName;

    @Column(length = 100)
    private String dosage;

    @Column(length = 200)
    private String frequency;

    @Column(nullable = false)
    private LocalTime reminderTime;

    @Column(length = 500)
    private String repeatDays;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 500)
    private String deviceIds;

    @Column(nullable = false)
    private Boolean enabled = true;

    private Boolean soundEnabled = true;

    private Boolean vibrationEnabled = true;

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
