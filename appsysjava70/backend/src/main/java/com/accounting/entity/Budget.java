package com.accounting.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "budgets")
@Schema(description = "预算实体")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "预算ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "预算名称", example = "2024年5月餐饮预算")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @Schema(description = "分类（为空表示总预算）")
    private Category category;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "预算金额", example = "2000.00")
    private BigDecimal amount;

    @Column(nullable = false)
    @Schema(description = "开始日期", example = "2024-05-01")
    private LocalDateTime startDate;

    @Column(nullable = false)
    @Schema(description = "结束日期", example = "2024-05-31")
    private LocalDateTime endDate;

    @Column(length = 200)
    @Schema(description = "备注", example = "本月餐饮预算")
    private String remark;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
