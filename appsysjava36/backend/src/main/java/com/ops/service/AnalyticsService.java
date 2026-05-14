package com.ops.service;

import com.ops.repository.UserRepository;
import com.ops.repository.WorkOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 数据分析服务类
 * 提供用户行为分析、服务效能监控等功能
 * 
 * @author ops-admin
 */
@Service
public class AnalyticsService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取服务效能统计
     */
    public Map<String, Object> getServiceMetrics(LocalDateTime start, LocalDateTime end) {
        Map<String, Object> metrics = new HashMap<>();
        
        long totalOrders = workOrderRepository.countByCreateTimeBetween(start, end);
        metrics.put("totalOrders", totalOrders);
        
        Double avgResponseTime = workOrderRepository.avgResponseDuration(start, end);
        metrics.put("avgResponseTime", avgResponseTime != null ? Math.round(avgResponseTime * 10) / 10.0 : 0);
        
        long completedOrders = workOrderRepository.countByStatus("COMPLETED");
        long firstFixCount = workOrderRepository.countFirstFix(start, end);
        double firstFixRate = completedOrders > 0 ? (double) firstFixCount / completedOrders * 100 : 0;
        metrics.put("firstFixRate", Math.round(firstFixRate * 10) / 10.0);
        
        Double avgSatisfaction = workOrderRepository.avgSatisfactionScore(start, end);
        metrics.put("avgSatisfaction", avgSatisfaction != null ? Math.round(avgSatisfaction * 10) / 10.0 : 0);
        
        long pendingOrders = workOrderRepository.countByStatus("PENDING");
        metrics.put("pendingOrders", pendingOrders);
        
        long inProgressOrders = workOrderRepository.countByStatus("IN_PROGRESS");
        metrics.put("inProgressOrders", inProgressOrders);
        
        return metrics;
    }

    /**
     * 获取用户行为分析
     */
    public Map<String, Object> getUserBehaviorAnalysis() {
        Map<String, Object> analysis = new HashMap<>();
        
        Map<String, Long> packageDistribution = new HashMap<>();
        packageDistribution.put("基础套餐", userRepository.countByPackageType("BASIC"));
        packageDistribution.put("标准套餐", userRepository.countByPackageType("STANDARD"));
        packageDistribution.put("高级套餐", userRepository.countByPackageType("PREMIUM"));
        analysis.put("packageDistribution", packageDistribution);
        
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        List<?> inactiveUsers = userRepository.findByLastLoginTimeBeforeAndIsActive(thirtyDaysAgo, true);
        analysis.put("inactiveUserCount", inactiveUsers.size());
        
        LocalDateTime sevenDaysLater = LocalDateTime.now().plusDays(7);
        List<?> expiringUsers = userRepository.findByExpireDateBeforeAndIsActive(sevenDaysLater, true);
        analysis.put("expiringUserCount", expiringUsers.size());
        
        return analysis;
    }

    /**
     * 获取工单趋势数据
     */
    public List<Map<String, Object>> getOrderTrend(int days) {
        List<Map<String, Object>> trend = new ArrayList<>();
        
        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime start = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime end = start.plusDays(1);
            long count = workOrderRepository.countByCreateTimeBetween(start, end);
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", start.toLocalDate().toString());
            dayData.put("count", count);
            trend.add(dayData);
        }
        
        return trend;
    }
}
