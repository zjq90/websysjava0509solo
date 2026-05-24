package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 调度任务实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "dispatch_task")
public class DispatchTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String taskNo;

    @Column(nullable = false, length = 20)
    private String taskType;

    @Column(nullable = false, length = 20)
    private String priority;

    @Column(nullable = false, length = 20)
    private String status;

    @Column
    private Long fromAreaId;

    @Column(length = 100)
    private String fromAreaName;

    @Column
    private Long toAreaId;

    @Column(length = 100)
    private String toAreaName;

    @Column(precision = 10, scale = 6)
    private Double targetLongitude;

    @Column(precision = 10, scale = 6)
    private Double targetLatitude;

    @Column(length = 200)
    private String targetLocation;

    @Column
    private Integer bikeCount;

    @Column(length = 1000)
    private String bikeIds;

    @Column
    private Long staffId;

    @Column(length = 50)
    private String staffName;

    @Column(length = 500)
    private String description;

    @Column
    private LocalDateTime createTime;

    @Column
    private LocalDateTime acceptTime;

    @Column
    private LocalDateTime completeTime;

    @Column
    private LocalDateTime deadline;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
