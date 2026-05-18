package com.flowerstore.backend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 订单项实体类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
@Schema(description = "订单项")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "订单项ID")
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "产品ID")
    private Long productId;

    @Column(length = 100)
    @Schema(description = "产品名称")
    private String productName;

    @Column(length = 500)
    @Schema(description = "产品图片")
    private String productImage;

    @Schema(description = "产品单价（分）")
    private Long productPrice;

    @Schema(description = "购买数量")
    private Integer quantity;

    @Schema(description = "小计金额（分）")
    private Long subtotal;

    @Schema(description = "是否已评价：0-否，1-是")
    private Integer isReviewed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (isReviewed == null) isReviewed = 0;
    }
}
