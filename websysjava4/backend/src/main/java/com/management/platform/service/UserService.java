package com.management.platform.service;

import com.management.platform.entity.User;
import com.management.platform.entity.UserBehavior;
import com.management.platform.repository.OrderRepository;
import com.management.platform.repository.UserBehaviorRepository;
import com.management.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务层
 * 提供用户的CRUD和统计分析功能
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBehaviorRepository userBehaviorRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 创建新用户
     * @param user 用户对象
     * @return 创建后的用户
     */
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户对象，不存在则返回null
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * 分页查询所有用户
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @return 分页结果
     */
    public Page<User> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    /**
     * 更新用户信息
     * @param id 用户ID
     * @param user 更新的用户信息
     * @return 更新后的用户
     */
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhone(user.getPhone());
            existingUser.setStatus(user.getStatus());
            existingUser.setPurchaseCount(user.getPurchaseCount());
            existingUser.setLastLoginTime(user.getLastLoginTime());
            return userRepository.save(existingUser);
        }
        return null;
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 是否删除成功
     */
    @Transactional
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 获取活跃用户数统计
     * @param days 统计天数
     * @return 活跃用户统计数据
     */
    public Map<String, Object> getActiveUsersStatistics(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime since = now.minusDays(days);
        
        Map<String, Object> result = new HashMap<>();
        long activeUsers = userBehaviorRepository.countActiveUsersBetween(since, now);
        long totalUsers = userRepository.count();
        long activeStatusUsers = userRepository.countByStatus("active");
        
        result.put("activeUsers", activeUsers);
        result.put("totalUsers", totalUsers);
        result.put("activeStatusUsers", activeStatusUsers);
        result.put("days", days);
        
        if (totalUsers > 0) {
            result.put("activeRate", (double) activeUsers / totalUsers * 100);
        } else {
            result.put("activeRate", 0.0);
        }
        
        return result;
    }

    /**
     * 获取复购率统计
     * @return 复购率统计数据
     */
    public Map<String, Object> getRepurchaseRateStatistics() {
        List<User> users = userRepository.findAll();
        Map<String, Object> result = new HashMap<>();
        
        int oneTimeBuyers = 0;
        int repeatBuyers = 0;
        
        for (User user : users) {
            long orderCount = orderRepository.countByUserId(user.getId());
            if (orderCount == 1) {
                oneTimeBuyers++;
            } else if (orderCount >= 2) {
                repeatBuyers++;
            }
        }
        
        int totalBuyers = oneTimeBuyers + repeatBuyers;
        double repurchaseRate = totalBuyers > 0 ? (double) repeatBuyers / totalBuyers * 100 : 0;
        
        result.put("oneTimeBuyers", oneTimeBuyers);
        result.put("repeatBuyers", repeatBuyers);
        result.put("totalBuyers", totalBuyers);
        result.put("repurchaseRate", repurchaseRate);
        
        return result;
    }

    /**
     * 获取新用户增长趋势（按日统计最近30天）
     * @return 新用户增长趋势数据
     */
    public List<Map<String, Object>> getNewUserGrowthTrend() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 29; i >= 0; i--) {
            LocalDateTime startOfDay = now.minusDays(i).truncatedTo(ChronoUnit.DAYS);
            LocalDateTime endOfDay = startOfDay.plusDays(1);
            
            long count = userRepository.countNewUsersBetween(startOfDay, endOfDay);
            
            Map<String, Object> map = new HashMap<>();
            map.put("date", startOfDay.toLocalDate().toString());
            map.put("newUsers", count);
            result.add(map);
        }
        
        return result;
    }

    /**
     * 获取购买时段分布热力图数据（按小时统计）
     * @param days 统计天数
     * @return 购买时段分布数据
     */
    public Map<String, Object> getPurchaseTimeHeatmap(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime since = now.minusDays(days);
        
        List<Object[]> hourData = orderRepository.countOrdersByHour(since, now);
        
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> hourlyData = new ArrayList<>();
        
        // 初始化24小时
        Map<Integer, Long> hourMap = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            hourMap.put(i, 0L);
        }
        
        for (Object[] row : hourData) {
            int hour = ((Number) row[0]).intValue();
            long count = ((Number) row[1]).longValue();
            hourMap.put(hour, count);
        }
        
        long totalOrders = 0;
        for (int i = 0; i < 24; i++) {
            long count = hourMap.get(i);
            totalOrders += count;
            
            Map<String, Object> map = new HashMap<>();
            map.put("hour", i);
            map.put("orderCount", count);
            hourlyData.add(map);
        }
        
        result.put("hourlyData", hourlyData);
        result.put("totalOrders", totalOrders);
        result.put("days", days);
        
        // 找出高峰时段
        long maxOrders = 0;
        int peakHour = 0;
        for (Map<String, Object> hd : hourlyData) {
            long count = (Long) hd.get("orderCount");
            if (count > maxOrders) {
                maxOrders = count;
                peakHour = (Integer) hd.get("hour");
            }
        }
        
        result.put("peakHour", peakHour);
        result.put("peakOrders", maxOrders);
        
        return result;
    }
}
