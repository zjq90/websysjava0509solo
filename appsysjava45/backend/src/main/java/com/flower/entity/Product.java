package com.flower.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(length = 255)
    private String image;

    @Column(length = 50)
    private String category;

    private Integer stock;

    @Column(columnDefinition = "int default 0")
    private Integer sales = 0;

    @Column(columnDefinition = "boolean default 0")
    private Boolean isHot = false;

    @Column(columnDefinition = "boolean default 0")
    private Boolean isNew = false;

    @Column(columnDefinition = "boolean default 1")
    private Boolean enabled = true;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (stock == null) stock = 0;
        if (originalPrice == null) originalPrice = price;
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}