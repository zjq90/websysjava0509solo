package com.flower.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 * 对应数据库表product
 */
@Data
@Entity
@Table(name = "product")
public class Product {

    /**
     * 商品ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 商品价格
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 库存数量
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     * 最低库存阈值（用于库存预警）
     */
    @Column(nullable = false)
    private Integer minStock = 10;

    /**
     * 商品描述
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * 商品图片URL（样片）
     */
    @Column(length = 500)
    private String imageUrl;

    /**
     * 商品分类
     */
    @Column(length = 50)
    private String category;

    /**
     * 上架状态：0-下架，1-上架
     */
    @Column(nullable = false)
    private Integer status = 1;

    /**
     * 是否需要定制：0-否，1-是
     */
    @Column(nullable = false)
    private Integer customFlag = 0;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
