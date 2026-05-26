package com.personal.accounting.entity;

import com.personal.accounting.entity.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易记录实体类
 * 记录每一笔收入或支出
 * 
 * @author Personal Accounting
 * @version 1.0.0
 */
@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "交易记录实体")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "交易ID", example = "1")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "交易类型", example = "EXPENSE")
    private TransactionType type;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "交易金额", example = "99.99")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "分类")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @Schema(description = "账户")
    private Account account;

    @Column(nullable = false)
    @Schema(description = "交易时间")
    private LocalDateTime transactionTime;

    @Column(length = 200)
    @Schema(description = "交易描述", example = "午餐")
    private String description;

    @Column(length = 500)
    @Schema(description = "备注", example = "公司附近快餐店")
    private String notes;

    @Column(length = 200)
    @Schema(description = "标签，逗号分隔", example = "午餐,工作餐")
    private String tags;

    @Column(length = 100)
    @Schema(description = "商家/交易对象", example = "麦当劳")
    private String merchant;

    @Column(length = 50)
    @Schema(description = "地理位置", example = "北京市朝阳区")
    private String location;

    @Column(name = "is_abnormal")
    @Schema(description = "是否为异常交易（大额支出等）", example = "false")
    private Boolean isAbnormal = false;

    @Column(name = "abnormal_reason", length = 200)
    @Schema(description = "异常原因", example = "超过月均支出3倍")
    private String abnormalReason;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (transactionTime == null) {
            transactionTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
