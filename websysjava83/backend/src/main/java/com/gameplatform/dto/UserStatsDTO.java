package com.gameplatform.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class UserStatsDTO {
    private Long dailyActiveUsers;
    private Long monthlyActiveUsers;
    private Long newUsers;
    private Double retentionRateDay1;
    private Double retentionRateDay7;
    private Double retentionRateDay30;
    private List<Map<String, Object>> regionDistribution;
}
