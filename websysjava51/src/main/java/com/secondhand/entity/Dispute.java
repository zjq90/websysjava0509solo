package com.secondhand.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_dispute")
@Schema(description = "纠纷实体")
public class Dispute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "纠纷ID")
    private Long id;

    @Schema(description = "订单ID")
    @Column(nullable = false)
    private Long orderId;

    @Schema(description = "订单编号")
    @Column(nullable = false, length = 50)
    private String orderNo;

    @Schema(description = "投诉人ID")
    @Column(nullable = false)
    private Long complainantId;

    @Schema(description = "被投诉人ID")
    @Column(nullable = false)
    private Long respondentId;

    @Schema(description = "纠纷类型: GOODS_ISSUE-商品问题, PAYMENT_ISSUE-支付问题, SHIPPING_ISSUE-物流问题, OTHER-其他")
    @Column(nullable = false, length = 30)
    private String type;

    @Schema(description = "纠纷标题")
    @Column(nullable = false, length = 200)
    private String title;

    @Schema(description = "纠纷描述")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Schema(description = "证据图片URL，多个用逗号分隔")
    @Column(length = 1000)
    private String evidenceImages;

    @Schema(description = "聊天记录")
    @Column(columnDefinition = "TEXT")
    private String chatHistory;

    @Schema(description = "纠纷状态: PENDING-待处理, PROCESSING-处理中, RESOLVED-已解决, CLOSED-已关闭")
    @Column(nullable = false, length = 30)
    private String status;

    @Schema(description = "仲裁结果")
    @Column(columnDefinition = "TEXT")
    private String arbitrationResult;

    @Schema(description = "仲裁人ID")
    private Long arbitratorId;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Schema(description = "处理时间")
    private LocalDateTime processTime;

    @Schema(description = "更新时间")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

}