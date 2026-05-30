package com.gameplatform.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class RevenueStatsDTO {
    private Double totalAdRevenue;
    private Double totalVipRevenue;
    private Double totalRevenue;
    private List<Map<String, Object>> revenueTrend;
}
