package com.secondhand.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_product")
@Schema(description = "商品实体")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "商品名称")
    @Column(nullable = false, length = 200)
    private String name;

    @Schema(description = "商品描述")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Schema(description = "商品价格")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Schema(description = "库存数量")
    @Column(nullable = false)
    private Integer stock;

    @Schema(description = "商品分类")
    @Column(length = 100)
    private String category;

    @Schema(description = "商品状态: ON_SALE-在售, OFF_SHELF-下架, DELETED-已删除")
    @Column(nullable = false, length = 20)
    private String status;

    @Schema(description = "商品图片URL")
    @Column(length = 500)
    private String imageUrl;

    @Schema(description = "卖家ID")
    @Column(nullable = false)
    private Long sellerId;

    @Schema(description = "成色: NEW-全新, LIKE_NEW-99新, EXCELLENT-95新, GOOD-9成新, FAIR-8成新")
    @Column(length = 20)
    private String condition;

    @Schema(description = "创建时间")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "ON_SALE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }

}