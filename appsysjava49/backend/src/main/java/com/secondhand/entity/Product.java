package com.secondhand.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(nullable = false)
    private Long categoryId;

    @Column(length = 50)
    private String brand;

    @Column(length = 50)
    private String condition;

    @Column(length = 200)
    private String location;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private Integer status = 1;

    private Integer viewCount = 0;

    private Integer favoriteCount = 0;

    private Integer shareCount = 0;

    private Boolean isNegotiable = true;

    private Boolean isDelivery = true;

    private Boolean isPickup = true;

    @Column(length = 500)
    private String images;

    @Column(length = 100)
    private String coverImage;

    @Column(updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private LocalDateTime refreshTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        refreshTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
