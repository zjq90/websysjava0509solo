package com.gamesys.service;

import com.gamesys.dto.DashboardStatsVO;
import com.gamesys.dto.TrendDataVO;
import com.gamesys.entity.DashboardConfig;

import java.util.List;

public interface DashboardService {
    DashboardStatsVO getTodayStats();
    TrendDataVO getTrendData(Integer days);
    List<DashboardConfig> getDashboardConfigs();
    DashboardConfig saveDashboardConfig(DashboardConfig config);
    void deleteDashboardConfig(Long id);
    void setDefaultDashboard(Long id);
    DashboardConfig getDefaultDashboard();
}
