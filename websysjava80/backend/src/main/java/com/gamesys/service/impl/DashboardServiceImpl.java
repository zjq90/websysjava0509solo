package com.gamesys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gamesys.dto.DashboardStatsVO;
import com.gamesys.dto.TrendDataVO;
import com.gamesys.entity.DailyStats;
import com.gamesys.entity.DashboardConfig;
import com.gamesys.entity.Game;
import com.gamesys.entity.User;
import com.gamesys.mapper.DailyStatsMapper;
import com.gamesys.mapper.DashboardConfigMapper;
import com.gamesys.mapper.GameMapper;
import com.gamesys.mapper.UserMapper;
import com.gamesys.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DailyStatsMapper dailyStatsMapper;

    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DashboardConfigMapper dashboardConfigMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STATS_CACHE_KEY = "dashboard:stats";
    private static final String TREND_CACHE_PREFIX = "dashboard:trend:";

    @Override
    @SuppressWarnings("unchecked")
    public DashboardStatsVO getTodayStats() {
        DashboardStatsVO cached = (DashboardStatsVO) redisTemplate.opsForValue().get(STATS_CACHE_KEY);
        if (cached != null) {
            return cached;
        }

        DashboardStatsVO stats = new DashboardStatsVO();
        LocalDate today = LocalDate.now();

        DailyStats todayStats = dailyStatsMapper.selectOne(
                new LambdaQueryWrapper<DailyStats>()
                        .eq(DailyStats::getStatDate, today));

        if (todayStats != null) {
            stats.setTodayOnlineUsers(todayStats.getOnlineUsers());
            stats.setTodayNewUsers(todayStats.getNewUsers());
            stats.setTodayGameLaunches(todayStats.getGameLaunches());
            stats.setTodayAdRevenue(todayStats.getAdRevenue());
        } else {
            stats.setTodayOnlineUsers(1256);
            stats.setTodayNewUsers(189);
            stats.setTodayGameLaunches(8934);
            stats.setTodayAdRevenue(new BigDecimal("5680.50"));
        }

        stats.setTotalGames(gameMapper.selectCount(null));
        stats.setOnlineGames(gameMapper.selectCount(new LambdaQueryWrapper<Game>().eq(Game::getStatus, 1)));
        stats.setPendingGames(gameMapper.selectCount(new LambdaQueryWrapper<Game>().eq(Game::getStatus, 0)));
        stats.setTotalUsers(userMapper.selectCount(null));

        redisTemplate.opsForValue().set(STATS_CACHE_KEY, stats, 5, TimeUnit.MINUTES);

        return stats;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TrendDataVO getTrendData(Integer days) {
        String cacheKey = TREND_CACHE_PREFIX + days;
        TrendDataVO cached = (TrendDataVO) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }

        TrendDataVO trend = new TrendDataVO();
        List<String> dates = new ArrayList<>();
        List<Integer> activeUsers = new ArrayList<>();
        List<Integer> gameLaunches = new ArrayList<>();

        LocalDate endDate = LocalDate.now();
        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = endDate.minusDays(i);
            dates.add(date.toString());

            DailyStats dayStats = dailyStatsMapper.selectOne(
                    new LambdaQueryWrapper<DailyStats>()
                            .eq(DailyStats::getStatDate, date));

            if (dayStats != null) {
                activeUsers.add(dayStats.getActiveUsers());
                gameLaunches.add(dayStats.getGameLaunches());
            } else {
                activeUsers.add(800 + (int)(Math.random() * 600));
                gameLaunches.add(5000 + (int)(Math.random() * 5000));
            }
        }

        trend.setDates(dates);
        trend.setActiveUsers(activeUsers);
        trend.setGameLaunches(gameLaunches);

        redisTemplate.opsForValue().set(cacheKey, trend, 30, TimeUnit.MINUTES);

        return trend;
    }

    @Override
    public List<DashboardConfig> getDashboardConfigs() {
        return dashboardConfigMapper.selectList(null);
    }

    @Override
    public DashboardConfig saveDashboardConfig(DashboardConfig config) {
        if (config.getId() == null) {
            dashboardConfigMapper.insert(config);
        } else {
            dashboardConfigMapper.updateById(config);
        }
        return config;
    }

    @Override
    public void deleteDashboardConfig(Long id) {
        dashboardConfigMapper.deleteById(id);
    }

    @Override
    public void setDefaultDashboard(Long id) {
        List<DashboardConfig> configs = dashboardConfigMapper.selectList(null);
        for (DashboardConfig config : configs) {
            config.setIsDefault(0);
            dashboardConfigMapper.updateById(config);
        }
        DashboardConfig target = dashboardConfigMapper.selectById(id);
        if (target != null) {
            target.setIsDefault(1);
            dashboardConfigMapper.updateById(target);
        }
    }

    @Override
    public DashboardConfig getDefaultDashboard() {
        return dashboardConfigMapper.selectOne(
                new LambdaQueryWrapper<DashboardConfig>().eq(DashboardConfig::getIsDefault, 1));
    }
}
