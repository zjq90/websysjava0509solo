package com.gameplatform.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "revenue_stats", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ad_slot_id", "stat_date"})
})
public class RevenueStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_slot_id", nullable = false)
    private Long adSlotId;

    @Column(name = "stat_date", nullable = false)
    private LocalDate statDate;

    @Column(name = "impressions", nullable = false)
    private Long impressions = 0L;

    @Column(name = "clicks", nullable = false)
    private Long clicks = 0L;

    @Column(name = "revenue", precision = 10, scale = 2, nullable = false)
    private BigDecimal revenue = BigDecimal.ZERO;

    @Column(name = "ecpm", precision = 10, scale = 2)
    private BigDecimal ecpm;

    @Transient
    private Double ctr;

    public Double getCtr() {
        if (impressions != null && impressions > 0) {
            return (clicks.doubleValue() / impressions.doubleValue()) * 100;
        }
        return 0.0;
    }

    public BigDecimal getEcpm() {
        if (impressions != null && impressions > 0) {
            return revenue.multiply(BigDecimal.valueOf(1000))
                    .divide(BigDecimal.valueOf(impressions), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }
}
