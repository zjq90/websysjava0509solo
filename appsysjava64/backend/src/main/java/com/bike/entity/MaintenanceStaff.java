package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 运维人员实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "maintenance_staff")
public class MaintenanceStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String staffNo;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 20)
    private String phone;

    @Column(length = 100)
    private String password;

    @Column(length = 20)
    private String avatar;

    @Column(length = 50)
    private String workArea;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(precision = 10, scale = 6)
    private Double longitude;

    @Column(precision = 10, scale = 6)
    private Double latitude;

    @Column(nullable = false)
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @Column
    private Integer completedTasks = 0;

    @Column
    private Float rating = 5.0f;

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
