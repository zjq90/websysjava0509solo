package com.flower.service;

import com.flower.repository.OrderItemRepository;
import com.flower.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据分析服务类
 */
@Service
public class AnalyticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    /**
     * 获取销售报表数据
     */
    public Map<String, Object> getSalesReport(String type) {
        Map<String, Object> result = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        LocalDateTime end = now;

        switch (type) {
            case "day":
                start = now.toLocalDate().atStartOfDay();
                break;
            case "week":
                start = now.minusWeeks(1);
                break;
            case "month":
                start = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
                break;
            default:
                start = now.minusDays(30);
        }

        BigDecimal totalSales = orderRepository.sumTotalAmountByCreateTimeBetween(start, end);
        Long orderCount = orderRepository.countByCreateTimeBetween(start, end);

        result.put("totalSales", totalSales);
        result.put("orderCount", orderCount);
        result.put("avgOrderValue", orderCount > 0 ? totalSales.divide(BigDecimal.valueOf(orderCount), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);

        return result;
    }

    /**
     * 获取热销商品
     */
    public List<Map<String, Object>> getHotProducts(int limit) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(30);

        List<Object[]> hotProducts = orderItemRepository.findHotProducts(start, end);
        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < Math.min(limit, hotProducts.size()); i++) {
            Object[] item = hotProducts.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("productId", item[0]);
            map.put("productName", item[1]);
            map.put("totalQuantity", item[2]);
            result.add(map);
        }

        return result;
    }

    /**
     * 获取销售趋势数据
     */
    public Map<String, Object> getSalesTrend(String type) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        LocalDate now = LocalDate.now();

        if ("day".equals(type)) {
            for (int i = 6; i >= 0; i--) {
                LocalDate date = now.minusDays(i);
                LocalDateTime start = date.atStartOfDay();
                LocalDateTime end = date.atTime(23, 59, 59);
                labels.add(date.getMonthValue() + "/" + date.getDayOfMonth());
                data.add(orderRepository.sumTotalAmountByCreateTimeBetween(start, end));
            }
        } else if ("week".equals(type)) {
            for (int i = 3; i >= 0; i--) {
                LocalDate weekStart = now.minusWeeks(i).with(java.time.DayOfWeek.MONDAY);
                LocalDate weekEnd = weekStart.plusDays(6);
                labels.add((i + 1) + "周前");
                data.add(orderRepository.sumTotalAmountByCreateTimeBetween(
                        weekStart.atStartOfDay(), weekEnd.atTime(23, 59, 59)));
            }
        } else if ("month".equals(type)) {
            for (int i = 5; i >= 0; i--) {
                LocalDate month = now.minusMonths(i);
                LocalDate monthStart = month.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate monthEnd = month.with(TemporalAdjusters.lastDayOfMonth());
                labels.add(month.getMonthValue() + "月");
                data.add(orderRepository.sumTotalAmountByCreateTimeBetween(
                        monthStart.atStartOfDay(), monthEnd.atTime(23, 59, 59)));
            }
        }

        result.put("labels", labels);
        result.put("data", data);
        return result;
    }
}
