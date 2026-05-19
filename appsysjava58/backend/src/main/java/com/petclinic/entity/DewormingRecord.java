package com.petclinic.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 驱虫记录实体类
 * 
 * @author PetClinic Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "deworming_records")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DewormingRecord extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "deworming_date", nullable = false)
    private LocalDate dewormingDate;

    @Column(name = "medicine_name", nullable = false, length = 100)
    private String medicineName;

    @Column(name = "dosage", length = 50)
    private String dosage;

    @Column(name = "next_deworming_date")
    private LocalDate nextDewormingDate;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "reminder_sent", nullable = false)
    private Boolean reminderSent = false;
}