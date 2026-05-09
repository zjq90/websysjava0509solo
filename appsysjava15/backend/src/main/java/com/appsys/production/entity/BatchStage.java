package com.appsys.production.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "batch_stage")
public class BatchStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long batchId;

    @Column(nullable = false, length = 50)
    private String stageCode;

    @Column(nullable = false, length = 100)
    private String stageName;

    @Column(nullable = false)
    private Integer sortOrder;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Long operatorId;

    @Column(length = 50)
    private String operatorName;

    @Column(length = 20)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String processParams;

    @Column(columnDefinition = "TEXT")
    private String remark;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (status == null) {
            status = "PENDING";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
