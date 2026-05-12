package com.hospital.clinic.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 电子病历实体类
 * 用于管理患者的病历信息
 */
@Data
@Entity
@Table(name = "medical_record")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_no", unique = true, nullable = false, length = 30)
    private String recordNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(name = "chief_complaint", length = 500)
    private String chiefComplaint;

    @Column(name = "present_illness", length = 2000)
    private String presentIllness;

    @Column(name = "past_history", length = 1000)
    private String pastHistory;

    @Column(name = "personal_history", length = 500)
    private String personalHistory;

    @Column(name = "family_history", length = 500)
    private String familyHistory;

    @Column(name = "physical_examination", length = 1000)
    private String physicalExamination;

    @Column(name = "temperature")
    private Double temperature;

    @Column(name = "pulse")
    private Integer pulse;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "respiration")
    private Integer respiration;

    @Column(name = "diagnosis", length = 1000)
    private String diagnosis;

    @Column(name = "treatment_plan", length = 1000)
    private String treatmentPlan;

    @Column(name = "remark", length = 1000)
    private String remark;

    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
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
