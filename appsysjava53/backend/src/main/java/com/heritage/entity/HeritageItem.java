package com.heritage.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 文物实体类
 *
 * @author Heritage Team
 * @version 1.0.0
 */
@Data
@Entity
@Table(name = "heritage_item")
public class HeritageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 50)
    private String category;

    @Column(length = 100)
    private String era;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String images;

    private String videos;

    @Column(name = "authenticity_cert")
    private String authenticityCert;

    @Column(name = "is_on_sale")
    private Integer isOnSale = 0;

    private BigDecimal price;

    private Integer status = 1;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = LocalDateTime.now();
        updatedTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}
