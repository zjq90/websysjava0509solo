package com.gamesys.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardStatsVO {
    private Integer todayOnlineUsers;
    private Integer todayNewUsers;
    private Integer todayGameLaunches;
    private BigDecimal todayAdRevenue;
    private Long totalGames;
    private Long onlineGames;
    private Long pendingGames;
    private Long totalUsers;
}
