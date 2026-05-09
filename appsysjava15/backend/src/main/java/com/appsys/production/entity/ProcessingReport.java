package com.appsys.production.entity;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "processing_report")
public class ProcessingReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long batchId;

    @Column(nullable = false, unique = true, length = 20)
    private String reportNo;

    @Column(nullable = false, length = 8)
    private String batchNo;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long generatedBy;

    @Column(length = 50)
    private String generatedByName;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
