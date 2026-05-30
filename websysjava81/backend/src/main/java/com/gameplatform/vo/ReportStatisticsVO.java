package com.gameplatform.vo;

import lombok.Data;
import java.util.Map;

@Data
public class ReportStatisticsVO {
    private Map<String, Long> typeDistribution;
    private Map<String, Long> statusDistribution;
    private Double avgHandleTime;
}
