package com.appsys.report.service;

import com.appsys.field.repository.FieldRecordRepository;
import com.appsys.inventory.entity.Inventory;
import com.appsys.inventory.entity.Seed;
import com.appsys.inventory.repository.InventoryRepository;
import com.appsys.inventory.repository.SeedRepository;
import com.appsys.order.entity.Order;
import com.appsys.order.repository.OrderRepository;
import com.appsys.report.dto.ReportDTO;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报表服务类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Service
public class ReportService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FieldRecordRepository fieldRecordRepository;

    /**
     * 获取综合报表数据
     */
    public ReportDTO getReport() {
        return ReportDTO.builder()
                .inventoryStats(getInventoryStats())
                .orderStats(getOrderStats())
                .fieldStats(getFieldStats())
                .build();
    }

    /**
     * 获取库存统计
     */
    private ReportDTO.InventoryStats getInventoryStats() {
        List<Inventory> inventories = inventoryRepository.findByDeletedFalseOrderByCreatedTimeDesc(
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent();
        List<Seed> seeds = seedRepository.findByDeletedFalseOrderByCreatedTimeDesc();

        // 计算总数量和总价值
        BigDecimal totalQuantity = inventories.stream()
                .map(Inventory::getRemainingQuantity)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalValue = inventories.stream()
                .map(i -> {
                    if (i.getRemainingQuantity() != null && i.getUnitPrice() != null) {
                        return i.getRemainingQuantity().multiply(i.getUnitPrice());
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 即将过期（3个月内过期）
        LocalDate today = LocalDate.now();
        LocalDate threeMonthsLater = today.plusMonths(3);
        long expiringCount = inventories.stream()
                .filter(i -> i.getExpiryDate() != null && 
                        !i.getExpiryDate().isBefore(threeMonthsLater))
                .count();

        // 按种子分类统计
        List<Map<String, Object>> bySeed = inventories.stream()
                .collect(Collectors.groupingBy(
                        Inventory::getSeedName, Collectors.summingDouble(i -> 
                                i.getRemainingQuantity() != null ? i.getRemainingQuantity().doubleValue() : 0.0)))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("seedName", entry.getKey());
                    map.put("quantity", BigDecimal.valueOf(entry.getValue()));
                    return map;
                })
                .collect(Collectors.toList());

        return ReportDTO.InventoryStats.builder()
                .totalQuantity(totalQuantity)
                .totalValue(totalValue)
                .seedCount((long) seeds.size())
                .expiringCount(expiringCount)
                .bySeed(bySeed)
                .build();
    }

    /**
     * 获取订单统计
     */
    private ReportDTO.OrderStats getOrderStats() {
        List<Order> orders = orderRepository.findByDeletedFalseOrderByCreatedTimeDesc(
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent();

        long totalOrders = orders.size();
        BigDecimal totalAmount = orders.stream()
                .map(Order::getActualAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingOrders = orders.stream()
                .filter(o -> o.getStatus() != null && o.getStatus() == 1)
                .count();

        long completedOrders = orders.stream()
                .filter(o -> o.getStatus() != null && o.getStatus() == 3)
                .count();

        return ReportDTO.OrderStats.builder()
                .totalOrders(totalOrders)
                .totalAmount(totalAmount)
                .pendingOrders(pendingOrders)
                .completedOrders(completedOrders)
                .monthlyData(new ArrayList<>())
                .build();
    }

    /**
     * 获取田间记录统计
     */
    private ReportDTO.FieldStats getFieldStats() {
        var records = fieldRecordRepository.findByDeletedFalseOrderByRecordDateDesc(
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent();

        long totalRecords = records.size();
        
        // 统计不同地块数
        long fieldCount = records.stream()
                .map(r -> r.getFieldName())
                .filter(Objects::nonNull)
                .distinct()
                .count();

        // 统计种植总面积
        BigDecimal totalArea = records.stream()
                .map(r -> r.getArea())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 按作物分类统计
        List<Map<String, Object>> byCrop = records.stream()
                .filter(r -> r.getCropName() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getCropName(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cropName", entry.getKey());
                    map.put("count", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return ReportDTO.FieldStats.builder()
                .totalRecords(totalRecords)
                .fieldCount(fieldCount)
                .totalArea(totalArea)
                .byCrop(byCrop)
                .build();
    }
}
