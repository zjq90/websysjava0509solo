package com.gameplatform.service;

import com.gameplatform.dto.*;
import com.gameplatform.entity.Game;
import com.gameplatform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private GamePlaySessionRepository gamePlaySessionRepository;

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private GameRepository gameRepository;

    public UserStatsDTO getUserStats(LocalDateTime startDate, LocalDateTime endDate) {
        UserStatsDTO dto = new UserStatsDTO();
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dayStart = now.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime monthStart = now.minusMonths(1);

        dto.setDailyActiveUsers(userActivityRepository.countDistinctUsersBetween(dayStart, now));
        dto.setMonthlyActiveUsers(userActivityRepository.countDistinctUsersBetween(monthStart, now));
        dto.setNewUsers(userRepository.countNewUsersBetween(startDate, endDate));
        
        dto.setRetentionRateDay1(calculateRetention(startDate, 1));
        dto.setRetentionRateDay7(calculateRetention(startDate, 7));
        dto.setRetentionRateDay30(calculateRetention(startDate, 30));

        List<Object[]> regionData = userRepository.countUsersByRegion();
        List<Map<String, Object>> regionDistribution = regionData.stream()
            .map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("region", row[0] != null ? row[0] : "未知");
                map.put("count", row[1]);
                return map;
            })
            .collect(Collectors.toList());
        dto.setRegionDistribution(regionDistribution);

        return dto;
    }

    private Double calculateRetention(LocalDateTime cohortStart, int days) {
        LocalDateTime cohortEnd = cohortStart.plusDays(1);
        Long newUsers = userRepository.countNewUsersBetween(cohortStart, cohortEnd);
        if (newUsers == null || newUsers == 0) {
            return 0.0;
        }

        LocalDateTime retentionStart = cohortStart.plusDays(days);
        LocalDateTime retentionEnd = retentionStart.plusDays(1);
        Long retainedUsers = userActivityRepository.countDistinctUsersBetween(retentionStart, retentionEnd);

        return Math.round((retainedUsers.doubleValue() / newUsers.doubleValue()) * 10000) / 100.0;
    }

    public GameStatsDTO getGameStats(LocalDateTime startDate, LocalDateTime endDate) {
        GameStatsDTO dto = new GameStatsDTO();
        dto.setTotalLaunches(gamePlaySessionRepository.countSessionsBetween(startDate, endDate));
        dto.setAveragePlayDuration(gamePlaySessionRepository.getAverageDurationBetween(startDate, endDate));

        List<Object[]> topGamesData = gamePlaySessionRepository.getTopGames(startDate, endDate);
        List<Map<String, Object>> topGames = topGamesData.stream()
            .limit(50)
            .map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("gameId", row[0]);
                map.put("gameName", row[1]);
                map.put("playCount", row[2]);
                return map;
            })
            .collect(Collectors.toList());
        dto.setTopGames(topGames);

        return dto;
    }

    public RevenueStatsDTO getRevenueStats(LocalDateTime startDate, LocalDateTime endDate) {
        RevenueStatsDTO dto = new RevenueStatsDTO();
        dto.setTotalAdRevenue(revenueRepository.getTotalAdRevenueBetween(startDate, endDate));
        dto.setTotalVipRevenue(revenueRepository.getTotalVipRevenueBetween(startDate, endDate));
        dto.setTotalRevenue(revenueRepository.getTotalRevenueBetween(startDate, endDate));

        List<Object[]> trendData = revenueRepository.getRevenueTrend(startDate, endDate);
        List<Map<String, Object>> revenueTrend = trendData.stream()
            .map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("date", row[0]);
                map.put("type", row[1]);
                map.put("amount", row[2]);
                return map;
            })
            .collect(Collectors.toList());
        dto.setRevenueTrend(revenueTrend);

        return dto;
    }

    public GameDetailStatsDTO getGameDetailStats(Long gameId, LocalDateTime startDate, LocalDateTime endDate) {
        Game game = gameRepository.findById(gameId).orElse(null);
        if (game == null) {
            return null;
        }

        GameDetailStatsDTO dto = new GameDetailStatsDTO();
        dto.setGameId(gameId);
        dto.setGameName(game.getName());

        List<Object[]> trendData = gamePlaySessionRepository.getLaunchTrendByGame(gameId, startDate, endDate);
        List<Map<String, Object>> launchTrend = trendData.stream()
            .map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("date", row[0]);
                map.put("count", row[1]);
                return map;
            })
            .collect(Collectors.toList());
        dto.setLaunchTrend(launchTrend);

        dto.setAveragePlayDuration(gamePlaySessionRepository.getAverageDurationBetween(startDate, endDate));

        Map<String, Double> retentionRates = new HashMap<>();
        retentionRates.put("day1", calculateRetention(startDate, 1));
        retentionRates.put("day7", calculateRetention(startDate, 7));
        retentionRates.put("day30", calculateRetention(startDate, 30));
        dto.setRetentionRates(retentionRates);

        List<Object[]> channelData = gamePlaySessionRepository.getSourceChannelDistribution(gameId, startDate, endDate);
        Map<String, Long> sourceChannels = new HashMap<>();
        for (Object[] row : channelData) {
            String channel = row[0] != null ? row[0].toString() : "未知";
            sourceChannels.put(channel, (Long) row[1]);
        }
        dto.setSourceChannelDistribution(sourceChannels);

        dto.setAdClickRate(gamePlaySessionRepository.getAdClickRate(gameId, startDate, endDate));
        dto.setPurchaseConversionRate(gamePlaySessionRepository.getPurchaseConversionRate(gameId, startDate, endDate));

        return dto;
    }

    public List<GameDetailStatsDTO> compareGames(List<Long> gameIds, LocalDateTime startDate, LocalDateTime endDate) {
        return gameIds.stream()
            .limit(5)
            .map(gameId -> getGameDetailStats(gameId, startDate, endDate))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    public List<Game> searchGames(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return gameRepository.findByIsActiveTrue();
        }
        return gameRepository.searchGames(keyword);
    }
}
