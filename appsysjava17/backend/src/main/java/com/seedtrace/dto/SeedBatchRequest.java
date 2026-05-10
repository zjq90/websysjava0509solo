package com.seedtrace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 种子批次请求DTO
 * 
 * <p>用于接收前端创建/更新批次的数据。</p>
 * 
 * @author Seed Trace System
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeedBatchRequest {
    
    private String batchCode;
    private String seedName;
    private String seedVariety;
    private BigDecimal germinationRate;
    private BigDecimal purity;
    private BigDecimal moistureContent;
    private LocalDate productionDate;
    private LocalDate shelfLife;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String status;
    private String createdBy;
}
