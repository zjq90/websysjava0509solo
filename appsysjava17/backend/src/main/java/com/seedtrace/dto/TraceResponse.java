package com.seedtrace.dto;

import com.seedtrace.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 溯源查询响应DTO
 * 
 * <p>封装完整的溯源信息，用于前端展示和PDF导出。</p>
 * 
 * <p>包含以下全链路信息：
 * <ul>
 *   <li>批次基本信息</li>
 *   <li>亲本来源信息</li>
 *   <li>田间管理记录（时间轴形式）</li>
 *   <li>加工流程记录</li>
 *   <li>质检报告</li>
 *   <li>销售记录</li>
 * </ul>
 * </p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraceResponse {
    
    /**
     * 批次基本信息
     */
    private SeedBatch batchInfo;
    
    /**
     * 亲本来源信息
     */
    private ParentInfo parentInfo;
    
    /**
     * 田间管理记录列表
     * 按时间顺序排列，用于时间轴展示
     */
    private List<FieldManagement> fieldRecords;
    
    /**
     * 加工流程记录列表
     */
    private List<ProcessingInfo> processingRecords;
    
    /**
     * 质检报告列表
     */
    private List<QualityReport> qualityReports;
    
    /**
     * 销售记录列表
     */
    private List<SalesInfo> salesRecords;
    
    /**
     * 查询时间戳
     */
    private String queryTime;
    
    /**
     * 查询唯一标识
     */
    private String queryId;
}
