package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "vaccine_record")
public class VaccineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "vaccine_name", nullable = false, length = 100)
    private String vaccineName;

    @Column(name = "vaccine_date")
    private LocalDate vaccineDate;

    @Column(name = "next_date")
    private LocalDate nextDate;

    @Column(length = 100)
    private String hospital;

    @Column(name = "doctor_name", length = 50)
    private String doctorName;

    @Column(length = 500)
    private String notes;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
