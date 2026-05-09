package com.example.websys.config;

import com.example.websys.entity.AdminUser;
import com.example.websys.entity.BusinessData;
import com.example.websys.entity.StatItem;
import com.example.websys.repository.AdminUserRepository;
import com.example.websys.repository.BusinessDataRepository;
import com.example.websys.repository.StatItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 测试数据初始化器
 * 应用启动时自动初始化测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdminUserRepository adminUserRepository;

    @Autowired
    private StatItemRepository statItemRepository;

    @Autowired
    private BusinessDataRepository businessDataRepository;

    private final Random random = new Random();

    @Override
    public void run(String... args) {
        System.out.println("========================================");
        System.out.println("  正在初始化测试数据...");
        System.out.println("========================================");

        initAdminUsers();
        initStatItems();
        initBusinessData();

        System.out.println("========================================");
        System.out.println("  测试数据初始化完成！");
        System.out.println("  默认管理员账号: admin / admin123");
        System.out.println("========================================");
    }

    private void initAdminUsers() {
        if (adminUserRepository.count() > 0) {
            return;
        }

        List<AdminUser> users = new ArrayList<>();

        AdminUser admin = new AdminUser();
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setRealName("系统管理员");
        admin.setEmail("admin@example.com");
        admin.setPhone("13800000000");
        admin.setRole("ADMIN");
        admin.setStatus(1);
        users.add(admin);

        AdminUser manager = new AdminUser();
        manager.setUsername("manager");
        manager.setPassword("manager123");
        manager.setRealName("运营经理");
        manager.setEmail("manager@example.com");
        manager.setPhone("13800000001");
        manager.setRole("MANAGER");
        manager.setStatus(1);
        users.add(manager);

        AdminUser ceo = new AdminUser();
        ceo.setUsername("ceo");
        ceo.setPassword("ceo123");
        ceo.setRealName("总经理");
        ceo.setEmail("ceo@example.com");
        ceo.setPhone("13800000002");
        ceo.setRole("CEO");
        ceo.setStatus(1);
        users.add(ceo);

        adminUserRepository.saveAll(users);
        System.out.println("  -> 初始化管理员用户: 3 条");
    }

    private void initStatItems() {
        if (statItemRepository.count() > 0) {
            return;
        }

        List<StatItem> items = new ArrayList<>();

        items.add(createStatItem("today_sales", "今日销售额", "amount", "销售模块", "元", "fa-money", "#28a745", true, 1, "今日累计销售总额"));
        items.add(createStatItem("today_orders", "今日订单数", "count", "订单模块", "单", "fa-shopping-cart", "#007bff", true, 2, "今日累计订单数量"));
        items.add(createStatItem("today_customers", "今日新增客户", "count", "客户模块", "人", "fa-users", "#17a2b8", true, 3, "今日新增客户数量"));
        items.add(createStatItem("today_visits", "今日访问量", "count", "访问统计", "次", "fa-eye", "#6f42c1", true, 4, "今日网站访问次数"));
        items.add(createStatItem("month_sales", "本月销售额", "amount", "销售模块", "元", "fa-chart-line", "#dc3545", false, 5, "本月累计销售总额"));
        items.add(createStatItem("month_orders", "本月订单数", "count", "订单模块", "单", "fa-file-invoice", "#fd7e14", false, 6, "本月累计订单数量"));
        items.add(createStatItem("total_users", "总用户数", "count", "用户模块", "人", "fa-user-plus", "#20c997", false, 7, "系统注册用户总数"));
        items.add(createStatItem("inventory_count", "库存商品数", "count", "库存模块", "件", "fa-warehouse", "#6c757d", false, 8, "当前库存商品总数"));
        items.add(createStatItem("pending_orders", "待处理订单", "count", "订单模块", "单", "fa-clock", "#ffc107", true, 9, "待处理的订单数量"));
        items.add(createStatItem("conversion_rate", "转化率", "percent", "统计模块", "%", "fa-percentage", "#e83e8c", false, 10, "访问转订单转化率"));

        statItemRepository.saveAll(items);
        System.out.println("  -> 初始化统计项: " + items.size() + " 条");
    }

    private StatItem createStatItem(String code, String name, String type, String source, String unit, 
                                     String icon, String color, boolean isDefault, int order, String desc) {
        StatItem item = new StatItem();
        item.setItemCode(code);
        item.setItemName(name);
        item.setItemType(type);
        item.setDataSource(source);
        item.setUnit(unit);
        item.setIcon(icon);
        item.setColor(color);
        item.setIsDefault(isDefault);
        item.setSortOrder(order);
        item.setStatus(1);
        item.setDescription(desc);
        return item;
    }

    private void initBusinessData() {
        if (businessDataRepository.count() > 0) {
            return;
        }

        LocalDate today = LocalDate.now();
        List<BusinessData> allData = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            allData.addAll(generateDailyData(date));
        }

        businessDataRepository.saveAll(allData);
        System.out.println("  -> 初始化经营数据: " + allData.size() + " 条 (最近7天)");
    }

    private List<BusinessData> generateDailyData(LocalDate date) {
        List<BusinessData> dataList = new ArrayList<>();

        int dayFactor = date.getDayOfWeek().getValue() <= 5 ? 1 : 2;

        BigDecimal todaySales = BigDecimal.valueOf(random.nextDouble() * 50000 * dayFactor + 10000).setScale(2, RoundingMode.HALF_UP);
        dataList.add(createBusinessData("today_sales", date, todaySales, random.nextDouble() * 30 - 15, 
                                        random.nextDouble() * 20 - 10, "今日累计销售总额"));

        int todayOrders = (int) (random.nextDouble() * 200 * dayFactor + 50);
        dataList.add(createBusinessData("today_orders", date, BigDecimal.valueOf(todayOrders), random.nextDouble() * 25 - 10,
                                        random.nextDouble() * 15 - 5, "今日订单数量统计"));

        int todayCustomers = (int) (random.nextDouble() * 100 * dayFactor + 20);
        dataList.add(createBusinessData("today_customers", date, BigDecimal.valueOf(todayCustomers), random.nextDouble() * 40 - 20,
                                        random.nextDouble() * 30 - 10, "今日新增客户数"));

        int todayVisits = (int) (random.nextDouble() * 5000 * dayFactor + 1000);
        dataList.add(createBusinessData("today_visits", date, BigDecimal.valueOf(todayVisits), random.nextDouble() * 20 - 8,
                                        random.nextDouble() * 15 - 5, "今日访问量统计"));

        BigDecimal monthSales = todaySales.multiply(BigDecimal.valueOf(random.nextDouble() * 0.5 + 0.8)).multiply(BigDecimal.valueOf(date.getDayOfMonth()));
        dataList.add(createBusinessData("month_sales", date, monthSales, random.nextDouble() * 15 - 5,
                                        random.nextDouble() * 10 - 3, "本月累计销售额"));

        int monthOrders = (int) (todayOrders * (random.nextDouble() * 0.5 + 0.8) * date.getDayOfMonth());
        dataList.add(createBusinessData("month_orders", date, BigDecimal.valueOf(monthOrders), random.nextDouble() * 12 - 4,
                                        random.nextDouble() * 8 - 2, "本月累计订单数"));

        int totalUsers = (int) (random.nextDouble() * 5000 + 2000);
        dataList.add(createBusinessData("total_users", date, BigDecimal.valueOf(totalUsers), random.nextDouble() * 10 - 2,
                                        random.nextDouble() * 5 - 1, "系统总用户数"));

        int inventoryCount = (int) (random.nextDouble() * 3000 + 1000);
        dataList.add(createBusinessData("inventory_count", date, BigDecimal.valueOf(inventoryCount), random.nextDouble() * 8 - 4,
                                        random.nextDouble() * 5 - 2, "库存商品总数"));

        int pendingOrders = (int) (random.nextDouble() * 50 + 10);
        dataList.add(createBusinessData("pending_orders", date, BigDecimal.valueOf(pendingOrders), random.nextDouble() * 30 - 10,
                                        random.nextDouble() * 20 - 5, "待处理订单数"));

        double conversionRate = random.nextDouble() * 8 + 2;
        dataList.add(createBusinessData("conversion_rate", date, BigDecimal.valueOf(conversionRate).setScale(2, RoundingMode.HALF_UP),
                                        random.nextDouble() * 20 - 10, random.nextDouble() * 15 - 5, "转化率统计"));

        return dataList;
    }

    private BusinessData createBusinessData(String statCode, LocalDate date, BigDecimal value,
                                             double trend, double compare, String remark) {
        BusinessData data = new BusinessData();
        data.setStatCode(statCode);
        data.setDataDate(date);
        data.setDataValue(value);
        data.setDataText(null);
        data.setTrendValue(BigDecimal.valueOf(trend).setScale(2, RoundingMode.HALF_UP));
        data.setTrendType(trend >= 0 ? "up" : "down");
        data.setCompareValue(BigDecimal.valueOf(compare).setScale(2, RoundingMode.HALF_UP));
        data.setCompareType(compare >= 0 ? "up" : "down");
        data.setRemark(remark);
        return data;
    }
}
