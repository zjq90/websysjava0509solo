package com.seedtrace.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 溯源查询日志实体类
 * 
 * <p>存储二维码扫描查询记录，用于统计分析和审计。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trace_log")
public class TraceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 批次ID（外键）
     */
    @Column(name = "batch_id", nullable = false)
    private Long batchId;

    /**
     * 批次编号
     */
    @Column(nullable = false, length = 8)
    private String batchCode;

    /**
     * 查询时间
     */
    @Column(updatable = false)
    private LocalDateTime queryTime;

    /**
     * 查询IP
     */
    @Column(length = 100)
    private String queryIp;

    /**
     * 查询设备信息
     */
    @Column(length = 200)
    private String queryDevice;

    /**
     * 查询位置
     */
    @Column(length = 200)
    private String queryLocation;

    /**
     * 浏览器/应用信息
     */
    @Column(length = 500)
    private String userAgent;

    /**
     * 返回结果数量
     */
    private Integer resultCount;

    @PrePersist
    protected void onCreate() {
        queryTime = LocalDateTime.now();
    }
}
