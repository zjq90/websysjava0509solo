package com.gameplatform.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class GameStatsDTO {
    private Long totalLaunches;
    private Double averagePlayDuration;
    private List<Map<String, Object>> topGames;
}
