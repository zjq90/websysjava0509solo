package com.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * 体检记录实体类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "checkup_records")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CheckupRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "checkup_date", nullable = false)
    private LocalDate checkupDate;

    @Column(name = "hospital_name", length = 100)
    private String hospitalName;

    @Column(name = "veterinarian_name", length = 50)
    private String veterinarianName;

    @Column(name = "weight", precision = 5, scale = 2)
    private java.math.BigDecimal weight;

    @Column(name = "temperature", precision = 4, scale = 2)
    private java.math.BigDecimal temperature;

    @Column(name = "heart_rate")
    private Integer heartRate;

    @Column(name = "blood_pressure", length = 20)
    private String bloodPressure;

    @Column(name = "general_health", length = 200)
    private String generalHealth;

    @Column(name = "next_checkup_date")
    private LocalDate nextCheckupDate;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "report_url", length = 255)
    private String reportUrl;

    @Column(name = "reminder_sent", nullable = false)
    private Boolean reminderSent = false;
}