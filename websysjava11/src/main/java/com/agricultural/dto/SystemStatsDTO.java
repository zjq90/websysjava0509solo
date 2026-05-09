package com.agricultural.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统统计信息DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemStatsDTO {
    private Long varietyCount;
    private Long customerCount;
    private Long orderCount;
    private Long financeCount;
    private Long costCount;
}
