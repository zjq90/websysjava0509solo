package com.accounting.entity;

import com.accounting.enums.BillType;
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
@Table(name = "bill_templates")
@Schema(description = "账单模板实体")
public class BillTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "模板ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "模板名称", example = "地铁通勤")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "账单类型")
    private BillType type;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "金额", example = "5.00")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "分类")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    @Schema(description = "账户")
    private Account account;

    @Column(length = 200)
    @Schema(description = "商家名称", example = "地铁")
    private String merchant;

    @Column(length = 500)
    @Schema(description = "备注", example = "上班通勤")
    private String remark;

    @Column(name = "use_count")
    @Schema(description = "使用次数", example = "10")
    private Integer useCount;

    @Column(name = "sort_order")
    @Schema(description = "排序", example = "1")
    private Integer sortOrder;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
