package com.websys.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 定义商品的基本信息，包括名称、价格、类别等
 */
@Entity
@Table(name = "biz_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "商品")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "商品ID")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    @Schema(description = "商品编码")
    private String productCode;

    @Column(nullable = false, length = 100)
    @Schema(description = "商品名称")
    private String productName;

    @Column(length = 100)
    @Schema(description = "商品类别")
    private String category;

    @Column(length = 200)
    @Schema(description = "商品描述")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @Schema(description = "售价")
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    @Schema(description = "成本价")
    private BigDecimal costPrice;

    @Column(nullable = false)
    @Schema(description = "状态：1上架，0下架")
    private Integer status = 1;

    @Column(name = "image_url", length = 500)
    @Schema(description = "商品图片URL")
    private String imageUrl;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
