package com.personal.accounting.entity;

import com.personal.accounting.enums.InvestmentType;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "investment")
public class Investment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentType investmentType;

    @Column(nullable = false)
    private String investmentName;

    private String code;

    @Column(precision = 15, scale = 4)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal costPrice;

    @Column(precision = 15, scale = 2)
    private BigDecimal currentPrice;

    @Column(precision = 15, scale = 2)
    private BigDecimal costAmount;

    @Column(precision = 15, scale = 2)
    private BigDecimal marketValue;

    @Column(precision = 15, scale = 2)
    private BigDecimal profitLoss;

    @Column(precision = 5, scale = 2)
    private BigDecimal profitLossRate;

    private String notes;

    private LocalDateTime purchaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateFields();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateFields();
    }

    private void calculateFields() {
        if (costPrice != null && quantity != null) {
            costAmount = costPrice.multiply(quantity);
        }
        if (currentPrice != null && quantity != null) {
            marketValue = currentPrice.multiply(quantity);
        }
        if (marketValue != null && costAmount != null) {
            profitLoss = marketValue.subtract(costAmount);
            if (costAmount.compareTo(BigDecimal.ZERO) > 0) {
                profitLossRate = profitLoss.divide(costAmount, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));
            }
        }
    }
}
