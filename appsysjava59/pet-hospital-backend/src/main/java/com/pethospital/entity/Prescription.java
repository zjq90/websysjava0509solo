package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prescription")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "consultation_id")
    private Long consultationId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "prescription_no", unique = true, length = 50)
    private String prescriptionNo;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(length = 20)
    private String status;

    @Column(length = 500)
    private String notes;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
