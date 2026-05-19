package com.pethospital.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "prescription_item")
public class PrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prescription_id", nullable = false)
    private Long prescriptionId;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Column(name = "medicine_name", nullable = false, length = 100)
    private String medicineName;

    @Column(length = 50)
    private String specification;

    private Integer quantity;

    @Column(length = 10)
    private String unit;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 50)
    private String dosage;

    @Column(length = 50)
    private String frequency;

    @Column(length = 50)
    private String duration;

    @Column(length = 200)
    private String notes;
}
