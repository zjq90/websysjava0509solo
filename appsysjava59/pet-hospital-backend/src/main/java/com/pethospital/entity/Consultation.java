package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "consultation")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 1000)
    private String symptom;

    @Column(name = "emergency_level", length = 20)
    private String emergencyLevel;

    @Column(length = 20)
    private String status;

    @Column(name = "consultation_type", length = 20)
    private String consultationType;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
