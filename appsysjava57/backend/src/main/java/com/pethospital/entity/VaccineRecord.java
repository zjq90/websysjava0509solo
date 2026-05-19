package com.pethospital.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 * 疫苗记录实体类
 * 存储宠物疫苗接种记录，支持到期提醒
 * 
 * @author Pet Hospital Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaccine_records")
public class VaccineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long petId;

    @Column(nullable = false, length = 100)
    private String vaccineName;

    @Column(nullable = false)
    private LocalDate vaccinationDate;

    private LocalDate expiryDate;

    @Column(length = 500)
    private String manufacturer;

    @Column(length = 500)
    private String batchNumber;

    @Column(length = 100)
    private String veterinary;

    @Column(length = 200)
    private String hospital;

    @Column(length = 500)
    private String notes;

    private Boolean reminderSent = false;

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
