package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 备件实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "spare_part")
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String partCode;

    @Column(nullable = false, length = 100)
    private String partName;

    @Column(length = 50)
    private String category;

    @Column(length = 100)
    private String specification;

    @Column(length = 200)
    private String unit;

    @Column
    private Float unitPrice;

    @Column
    private Integer stockQuantity;

    @Column
    private Integer minStock;

    @Column(length = 50)
    private String status;

    @Column(length = 500)
    private String supplier;

    @Column
    private LocalDateTime createTime;

    @Column
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
