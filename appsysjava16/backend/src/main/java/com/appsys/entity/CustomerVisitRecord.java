package com.appsys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客户回访记录实体�? * 记录业务员对客户的回访信�? * 
 * @author appsys-team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_visit_records")
@Schema(description = "客户回访记录")
public class CustomerVisitRecord {

    /**
     * 回访记录ID，自增主�?     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "回访记录ID", example = "1")
    private Long id;

    /**
     * 关联客户ID
     */
    @Column(nullable = false)
    @Schema(description = "客户ID", example = "1")
    private Long customerId;

    /**
     * 回访业务员ID
     */
    @Schema(description = "业务员ID", example = "1")
    private Long salespersonId;

    /**
     * 回访业务员姓�?     */
    @Column(length = 50)
    @Schema(description = "业务员姓�?, example = "李四")
    private String salespersonName;

    /**
     * 回访方式
     */
    @Column(nullable = false, length = 20)
    @Schema(description = "回访方式", example = "电话回访")
    private String visitType;

    /**
     * 回访时间
     */
    @Column(nullable = false)
    @Schema(description = "回访时间", example = "2026-05-10T10:30:00")
    private LocalDateTime visitTime;

    /**
     * 回访内容
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "回访内容", example = "客户对产品很满意，有复购意向")
    private String content;

    /**
     * 客户反馈
     */
    @Column(columnDefinition = "TEXT")
    @Schema(description = "客户反馈", example = "希望能有更多优惠")
    private String feedback;

    /**
     * 下次回访时间
     */
    @Schema(description = "下次回访时间")
    private LocalDateTime nextVisitTime;

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
