package com.personal.accounting.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "virtual_account")
public class VirtualAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long familyId;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentBalance;

    @Column(precision = 15, scale = 2)
    private BigDecimal targetAmount;

    private LocalDate targetDate;

    private String purpose;

    private String color;

    private String icon;

    private Boolean isFamilyAccount = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isFamilyAccount == null) {
            isFamilyAccount = false;
        }
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
