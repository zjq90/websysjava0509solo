package com.personal.accounting.entity;

import com.personal.accounting.enums.BudgetCategory;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "budget", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"familyId", "category", "budgetMonth"})
})
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long familyId;

    private Long userId;

    @Column(nullable = false)
    private String budgetName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BudgetCategory category;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal spentAmount = BigDecimal.ZERO;

    @Transient
    private BigDecimal remainingAmount;

    @Column(nullable = false)
    private YearMonth budgetMonth;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public BigDecimal getRemainingAmount() {
        if (totalAmount == null || spentAmount == null) {
            return BigDecimal.ZERO;
        }
        return totalAmount.subtract(spentAmount);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (spentAmount == null) {
            spentAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
