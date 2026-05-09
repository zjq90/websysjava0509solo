package com.example.websys.controller;

import com.example.websys.dto.Result;
import com.example.websys.entity.BusinessData;
import com.example.websys.entity.StatItem;
import com.example.websys.entity.UserDashboardConfig;
import com.example.websys.repository.AdminUserRepository;
import com.example.websys.repository.BusinessDataRepository;
import com.example.websys.repository.StatItemRepository;
import com.example.websys.repository.UserDashboardConfigRepository;
import com.example.websys.service.UserDashboardConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 测试功能REST API控制器
 * 提供测试数据生成、数据清除、一键重置等测试辅助功能
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private StatItemRepository statItemRepository;

    @Autowired
    private BusinessDataRepository businessDataRepository;

    @Autowired
    private UserDashboardConfigRepository userDashboardConfigRepository;

    @Autowired
    private UserDashboardConfigService userDashboardConfigService;

    private final Random random = new Random();

    @GetMapping("/status")
    public Result<Map<String, Long>> getDatabaseStatus() {
        Map<String, Long> status = new HashMap<>();
        status.put("adminUsers", adminUserRepository.count());
        status.put("statItems", statItemRepository.count());
        status.put("businessData", businessDataRepository.count());
        status.put("userConfigs", userDashboardConfigRepository.count());
        return Result.success(status);
    }

    @PostMapping("/generate-business-data")
    public Result<String> generateBusinessData(
            @RequestParam(defaultValue = "30") int days) {
        
        List<StatItem> items = statItemRepository.findAll();
        if (items.isEmpty()) {
            return Result.error("请先初始化统计项");
        }

        LocalDate today = LocalDate.now();
        List<BusinessData> allData = new ArrayList<>();

        for (int i = 0; i < days; i++) {
            LocalDate date = today.minusDays(i);
            allData.addAll(generateDailyData(date, items));
        }

        businessDataRepository.saveAll(allData);
        return Result.success("成功生成 " + days + " 天的经营数据，共 " + allData.size() + " 条记录");
    }

    @PostMapping("/clear-business-data")
    public Result<String> clearBusinessData() {
        long count = businessDataRepository.count();
        businessDataRepository.deleteAll();
        return Result.success("已清除 " + count + " 条经营数据");
    }

    @PostMapping("/reset-all")
    public Result<String> resetAll() {
        userDashboardConfigRepository.deleteAll();
        businessDataRepository.deleteAll();
        statItemRepository.deleteAll();
        adminUserRepository.deleteAll();
        return Result.success("已清空所有数据，请重新启动应用初始化");
    }

    @PostMapping("/init-user-config/{userId}")
    public Result<List<UserDashboardConfig>> initUserConfig(@PathVariable Long userId) {
        List<UserDashboardConfig> configs = userDashboardConfigService.initDefaultConfig(userId);
        return Result.success(configs);
    }

    @PostMapping("/generate-one-day")
    public Result<String> generateOneDayData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        
        List<StatItem> items = statItemRepository.findAll();
        if (items.isEmpty()) {
            return Result.error("请先初始化统计项");
        }

        List<BusinessData> dataList = generateDailyData(date, items);
        for (BusinessData data : dataList) {
            businessDataRepository.findByStatCodeAndDataDate(data.getStatCode(), date)
                    .ifPresent(existing -> data.setId(existing.getId()));
        }
        businessDataRepository.saveAll(dataList);
        return Result.success("成功生成 " + date + " 的经营数据，共 " + dataList.size() + " 条");
    }

    @PostMapping("/update-random-data")
    public Result<String> updateRandomData() {
        List<BusinessData> todayData = businessDataRepository.findByDataDateOrderByIdAsc(LocalDate.now());
        int updatedCount = 0;

        for (BusinessData data : todayData) {
            BigDecimal currentValue = data.getDataValue();
            if (currentValue != null) {
                double factor = 0.9 + random.nextDouble() * 0.2;
                BigDecimal newValue = currentValue.multiply(BigDecimal.valueOf(factor)).setScale(2, RoundingMode.HALF_UP);
                data.setDataValue(newValue);
                data.setTrendValue(BigDecimal.valueOf(random.nextDouble() * 40 - 20).setScale(2, RoundingMode.HALF_UP));
                businessDataRepository.save(data);
                updatedCount++;
            }
        }

        return Result.success("已随机更新 " + updatedCount + " 条今日数据，模拟实时变化");
    }

    private List<BusinessData> generateDailyData(LocalDate date, List<StatItem> items) {
        List<BusinessData> dataList = new ArrayList<>();
        int dayFactor = date.getDayOfWeek().getValue() <= 5 ? 1 : 2;

        for (StatItem item : items) {
            if (item.getStatus() != 1) continue;

            BusinessData data = new BusinessData();
            data.setStatCode(item.getItemCode());
            data.setDataDate(date);
            data.setRemark("测试数据 - " + item.getItemName());

            switch (item.getItemCode()) {
                case "today_sales":
                    data.setDataValue(BigDecimal.valueOf(random.nextDouble() * 50000 * dayFactor + 10000).setScale(2, RoundingMode.HALF_UP));
                    break;
                case "today_orders":
                    data.setDataValue(BigDecimal.valueOf((int) (random.nextDouble() * 200 * dayFactor + 50)));
                    break;
                case "today_customers":
                    data.setDataValue(BigDecimal.valueOf((int) (random.nextDouble() * 100 * dayFactor + 20)));
                    break;
                case "today_visits":
                    data.setDataValue(BigDecimal.valueOf((int) (random.nextDouble() * 5000 * dayFactor + 1000)));
                    break;
                case "month_sales":
                    data.setDataValue(BigDecimal.valueOf(random.nextDouble() * 500000 + 200000).setScale(2, RoundingMode.HALF_UP));
                    break;
                case "month_orders":
                    data.setDataValue(BigDecimal.valueOf((int) (random.nextDouble() * 2000 + 500)));
                    break;
                case "total_users":
                    data.setDataValue(BigDecimal.valueOf((int) (random.nextDouble() * 5000 + 2000)));
                    break;
                case "inventory_count":
                    data.setDataValue(BigDecimal.valueOf((int) (random.nextDouble() * 3000 + 1000)));
                    break;
                case "pending_orders":
                    data.setDataValue(BigDecimal.valueOf((int) (random.nextDouble() * 50 + 10)));
                    break;
                case "conversion_rate":
                    data.setDataValue(BigDecimal.valueOf(random.nextDouble() * 8 + 2).setScale(2, RoundingMode.HALF_UP));
                    break;
                default:
                    data.setDataValue(BigDecimal.valueOf(random.nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP));
            }

            double trend = random.nextDouble() * 40 - 20;
            data.setTrendValue(BigDecimal.valueOf(trend).setScale(2, RoundingMode.HALF_UP));
            data.setTrendType(trend >= 0 ? "up" : "down");

            double compare = random.nextDouble() * 30 - 15;
            data.setCompareValue(BigDecimal.valueOf(compare).setScale(2, RoundingMode.HALF_UP));
            data.setCompareType(compare >= 0 ? "up" : "down");

            dataList.add(data);
        }

        return dataList;
    }
}
