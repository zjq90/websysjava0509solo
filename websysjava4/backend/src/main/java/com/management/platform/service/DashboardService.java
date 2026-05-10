package com.management.platform.service;

import com.management.platform.repository.DeviceFaultRepository;
import com.management.platform.repository.DeviceRepository;
import com.management.platform.repository.OrderRepository;
import com.management.platform.repository.ProductRepository;
import com.management.platform.repository.UserBehaviorRepository;
import com.management.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 大屏数据服务层
 * 提供数据大屏所需的综合统计数据
 */
@Service
public class DashboardService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeviceFaultRepository deviceFaultRepository;

    @Autowired
    private UserBehaviorRepository userBehaviorRepository;

    /**
     * 获取大屏综合数据
     * @return 大屏综合统计数据
     */
    public Map<String, Object> getDashboardData() {
        Map<String, Object> result = new HashMap<>();
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime yesterdayStart = todayStart.minusDays(1);
        
        // 销售数据
        BigDecimal totalRevenue = orderRepository.sumTotalAmount();
        long totalOrders = orderRepository.countCompletedOrders();
        BigDecimal todayRevenue = getRevenueBetween(todayStart, now);
        BigDecimal yesterdayRevenue = getRevenueBetween(yesterdayStart, todayStart);
        
        // 计算今日同比增长
        BigDecimal revenueGrowth = BigDecimal.ZERO;
        if (yesterdayRevenue.compareTo(BigDecimal.ZERO) > 0) {
            revenueGrowth = todayRevenue.subtract(yesterdayRevenue)
                .multiply(new BigDecimal(100))
                .divide(yesterdayRevenue, 2, BigDecimal.ROUND_HALF_UP);
        }
        
        Map<String, Object> salesData = new HashMap<>();
        salesData.put("totalRevenue", totalRevenue);
        salesData.put("totalOrders", totalOrders);
        salesData.put("todayRevenue", todayRevenue);
        salesData.put("yesterdayRevenue", yesterdayRevenue);
        salesData.put("revenueGrowth", revenueGrowth);
        result.put("sales", salesData);
        
        // 商品数据
        long totalProducts = productRepository.count();
        Map<String, Object> productData = new HashMap<>();
        productData.put("total", totalProducts);
        result.put("products", productData);
        
        // 设备数据
        long totalDevices = deviceRepository.count();
        long runningDevices = deviceRepository.countByStatus("running");
        long totalFaults = deviceFaultRepository.countTotalFaults();
        BigDecimal totalMaintenanceCost = deviceFaultRepository.sumTotalRepairCost();
        
        Map<String, Object> deviceData = new HashMap<>();
        deviceData.put("total", totalDevices);
        deviceData.put("running", runningDevices);
        deviceData.put("faults", totalFaults);
        deviceData.put("maintenanceCost", totalMaintenanceCost);
        if (totalDevices > 0) {
            deviceData.put("utilizationRate", (double) runningDevices / totalDevices * 100);
        } else {
            deviceData.put("utilizationRate", 0.0);
        }
        result.put("devices", deviceData);
        
        // 用户数据
        long totalUsers = userRepository.count();
        long activeUsers = userBehaviorRepository.countActiveUsersBetween(todayStart.minusDays(7), now);
        long newUsersToday = userRepository.countNewUsersBetween(todayStart, now);
        
        Map<String, Object> userData = new HashMap<>();
        userData.put("total", totalUsers);
        userData.put("active7days", activeUsers);
        userData.put("newToday", newUsersToday);
        if (totalUsers > 0) {
            userData.put("activeRate", (double) activeUsers / totalUsers * 100);
        } else {
            userData.put("activeRate", 0.0);
        }
        result.put("users", userData);
        
        // 实时时间
        result.put("updateTime", now.toString());
        
        return result;
    }

    /**
     * 计算指定时间范围内的销售额
     */
    private BigDecimal getRevenueBetween(LocalDateTime start, LocalDateTime end) {
        return orderRepository.findByPurchaseTimeBetween(start, end).stream()
            .filter(o -> "completed".equals(o.getStatus()))
            .map(o -> o.getTotalAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
