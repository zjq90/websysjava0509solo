package com.personal.accounting.entity;

import com.personal.accounting.enums.DebtType;
import com.personal.accounting.enums.RepaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "debt")
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebtType debtType;

    @Column(nullable = false)
    private String debtName;

    private String creditor;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal remainingAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal annualInterestRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepaymentMethod repaymentMethod;

    private Integer loanTermMonths;

    @Column(precision = 15, scale = 2)
    private BigDecimal monthlyPayment;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate nextPaymentDate;

    private Integer paymentDay;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalInterestPaid;

    private String notes;

    private Boolean isPaidOff = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isPaidOff == null) {
            isPaidOff = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
