package com.gameplatform.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GameDetailStatsDTO {
    private Long gameId;
    private String gameName;
    private List<Map<String, Object>> launchTrend;
    private Double averagePlayDuration;
    private Map<String, Double> retentionRates;
    private Map<String, Long> sourceChannelDistribution;
    private Double adClickRate;
    private Double purchaseConversionRate;
}
