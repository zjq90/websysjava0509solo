package com.ops.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 装维人员实体类
 * 存储装维人员的基本信息和位置信息
 * 
 * @author ops-admin
 */
@Data
@Entity
@Table(name = "sys_technician")
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "work_area", length = 100)
    private String workArea;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "current_load")
    private Integer currentLoad;

    @Column(name = "max_load")
    private Integer maxLoad;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "total_orders")
    private Integer totalOrders;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        currentLoad = 0;
        maxLoad = 10;
        totalOrders = 0;
        rating = 5.0;
        status = "ONLINE";
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
