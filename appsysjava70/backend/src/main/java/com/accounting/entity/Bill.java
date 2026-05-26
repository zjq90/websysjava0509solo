package com.accounting.entity;

import com.accounting.enums.BillType;
import com.accounting.enums.SyncStatus;
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
@Table(name = "bills")
@Schema(description = "账单实体")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "账单ID", example = "1")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Schema(description = "账单类型")
    private BillType type;

    @Column(precision = 15, scale = 2, nullable = false)
    @Schema(description = "金额", example = "35.00")
    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @Schema(description = "分类")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    @Schema(description = "账户")
    private Account account;

    @Column(nullable = false)
    @Schema(description = "交易时间")
    private LocalDateTime transactionTime;

    @Column(length = 200)
    @Schema(description = "商家名称", example = "星巴克")
    private String merchant;

    @Column(length = 500)
    @Schema(description = "备注", example = "和朋友喝咖啡")
    private String remark;

    @Column(length = 500)
    @Schema(description = "图片URL", example = "https://example.com/receipt.jpg")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Schema(description = "同步状态")
    private SyncStatus syncStatus;

    @Column(length = 100)
    @Schema(description = "设备ID，用于离线同步")
    private String deviceId;

    @Column(length = 100)
    @Schema(description = "客户端唯一标识，用于去重")
    private String clientId;

    @Column(name = "is_deleted")
    @Schema(description = "是否删除", example = "false")
    private Boolean deleted;

    @CreationTimestamp
    @Column(updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}
