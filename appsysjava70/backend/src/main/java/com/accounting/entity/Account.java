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
@Table(name = "accounts")
@Schema(description = "账户实体")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "账户ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "账户名称", example = "招商银行储蓄卡")
    private String name;

    @Column(length = 100)
    @Schema(description = "账户图标", example = "🏦")
    private String icon;

    @Column(precision = 15, scale = 2)
    @Schema(description = "账户余额", example = "10000.00")
    private BigDecimal balance;

    @Column(length = 50)
    @Schema(description = "账户类型", example = "储蓄卡")
    private String type;

    @Column(length = 200)
    @Schema(description = "备注", example = "工资卡")
    private String remark;

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
