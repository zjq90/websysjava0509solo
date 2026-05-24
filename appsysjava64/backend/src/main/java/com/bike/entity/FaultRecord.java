package com.bike.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 故障记录实体类
 * 
 * @author bike-sharing
 */
@Data
@Entity
@Table(name = "fault_record")
public class FaultRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String recordNo;

    @Column(nullable = false)
    private Long bikeId;

    @Column(length = 50)
    private String bikeNo;

    @Column(nullable = false, length = 50)
    private String faultType;

    @Column(length = 500)
    private String faultDescription;

    @Column(length = 500)
    private String faultImages;

    @Column(length = 20)
    private String status;

    @Column
    private Long reporterId;

    @Column(length = 50)
    private String reporterName;

    @Column(length = 20)
    private String reporterType;

    @Column
    private LocalDateTime reportTime;

    @Column
    private LocalDateTime createTime;

    @Column
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (reportTime == null) {
            reportTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
