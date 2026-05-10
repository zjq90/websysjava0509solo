package com.management.platform.init;

import com.management.platform.entity.*;
import com.management.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * 数据初始化器
 * 应用启动时自动生成测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

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

    private final Random random = new Random();

    @Override
    public void run(String... args) {
        initProducts();
        initDevices();
        initUsers();
        initOrders();
        initDeviceFaults();
        initUserBehaviors();
    }

    /**
     * 初始化商品数据
     */
    private void initProducts() {
        String[] categories = {"电子产品", "食品饮料", "服装鞋帽", "家居用品", "美妆个护"};
        String[] productNames = {
            "智能手机", "笔记本电脑", "平板电脑", "蓝牙耳机", "智能手表",
            "矿泉水", "碳酸饮料", "果汁饮料", "方便面", "饼干",
            "T恤", "牛仔裤", "运动鞋", "外套", "连衣裙",
            "台灯", "枕头", "被子", "收纳盒", "窗帘",
            "洗面奶", "面膜", "防晒霜", "洗发水", "护发素"
        };

        for (int i = 0; i < 25; i++) {
            Product product = new Product();
            product.setName(productNames[i]);
            product.setCategory(categories[i / 5]);
            
            BigDecimal costPrice = BigDecimal.valueOf(random.nextInt(900) + 100);
            BigDecimal markup = BigDecimal.valueOf(random.nextInt(50) + 30)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            BigDecimal salePrice = costPrice.multiply(BigDecimal.ONE.add(markup))
                .setScale(2, RoundingMode.HALF_UP);
            
            product.setCostPrice(costPrice);
            product.setSalePrice(salePrice);
            product.setStockQuantity(random.nextInt(500) + 50);
            product.setSalesQuantity(random.nextInt(200));
            product.setStatus(i > 20 ? "inactive" : "active");
            
            productRepository.save(product);
        }
    }

    /**
     * 初始化设备数据
     */
    private void initDevices() {
        String[] types = {"生产设备", "运输设备", "检测设备", "包装设备", "仓储设备"};
        String[] locations = {"A车间", "B车间", "C车间", "仓库A", "仓库B", "质检中心"};
        String[] deviceNames = {
            "数控车床", "铣床", "磨床", "钻床", "冲压机",
            "叉车", "搬运车", "升降机", "传送带", "AGV小车",
            "检测仪", "测试台", "分析仪", "传感器", "校准器",
            "封口机", "贴标机", "包装机", "打码机", "装箱机",
            "货架系统", "分拣机", "堆垛机", "输送机", "码垛机"
        };

        for (int i = 0; i < 20; i++) {
            Device device = new Device();
            device.setDeviceCode("DEV" + String.format("%04d", i + 1));
            device.setName(deviceNames[i]);
            device.setType(types[i / 4]);
            device.setLocation(locations[random.nextInt(locations.length)]);
            device.setStatus(i > 17 ? "maintenance" : "running");
            device.setTotalRuntime((double) (random.nextInt(8000) + 1000));
            device.setTotalOutput((long) (random.nextInt(100000) + 10000));
            device.setFaultCount(random.nextInt(10));
            device.setMaintenanceCost(BigDecimal.valueOf(random.nextInt(50000) + 1000));
            
            deviceRepository.save(device);
        }
    }

    /**
     * 初始化用户数据
     */
    private void initUsers() {
        String[] firstNames = {"张", "李", "王", "刘", "陈", "杨", "赵", "黄", "周", "吴"};
        String[] lastNames = {"伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军"};

        for (int i = 0; i < 50; i++) {
            User user = new User();
            user.setUsername("user" + (i + 1));
            user.setEmail("user" + (i + 1) + "@example.com");
            user.setPhone("138" + String.format("%08d", random.nextInt(100000000)));
            user.setStatus(random.nextInt(10) > 2 ? "active" : "inactive");
            user.setPurchaseCount(random.nextInt(20));
            
            // 生成过去30天内的注册时间
            LocalDateTime createdAt = LocalDateTime.now().minusDays(random.nextInt(30));
            user.setCreatedAt(createdAt);
            user.setUpdatedAt(createdAt);
            
            // 最近登录时间
            if (user.getStatus().equals("active")) {
                user.setLastLoginTime(LocalDateTime.now().minusDays(random.nextInt(7)));
            } else {
                user.setLastLoginTime(LocalDateTime.now().minusDays(random.nextInt(30) + 30));
            }
            
            userRepository.save(user);
        }
    }

    /**
     * 初始化订单数据
     */
    private void initOrders() {
        for (int i = 0; i < 100; i++) {
            Order order = new Order();
            order.setOrderNo("ORD" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
            order.setUserId((long) (random.nextInt(50) + 1));
            order.setProductId((long) (random.nextInt(20) + 1));
            order.setQuantity(random.nextInt(5) + 1);
            
            // 随机生成购买时间（过去7天内，不同时段）
            LocalDateTime purchaseTime = LocalDateTime.now()
                .minusDays(random.nextInt(7))
                .withHour(random.nextInt(24))
                .withMinute(random.nextInt(60));
            order.setPurchaseTime(purchaseTime);
            order.setCreatedAt(purchaseTime);
            
            // 计算订单金额
            Product product = productRepository.findById(order.getProductId()).orElse(null);
            if (product != null) {
                order.setTotalAmount(product.getSalePrice()
                    .multiply(BigDecimal.valueOf(order.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP));
            } else {
                order.setTotalAmount(BigDecimal.valueOf(random.nextInt(1000) + 100));
            }
            
            order.setStatus(random.nextInt(10) > 1 ? "completed" : "cancelled");
            
            orderRepository.save(order);
        }
    }

    /**
     * 初始化设备故障记录
     */
    private void initDeviceFaults() {
        String[] severities = {"low", "medium", "high"};
        String[] descriptions = {
            "设备正常磨损", "传感器故障", "电机过热", "电路短路",
            "机械部件损坏", "软件异常", "通信中断", "液压系统故障"
        };

        for (int i = 0; i < 30; i++) {
            DeviceFault fault = new DeviceFault();
            fault.setDeviceId((long) (random.nextInt(20) + 1));
            fault.setDescription(descriptions[random.nextInt(descriptions.length)]);
            fault.setSeverity(severities[random.nextInt(severities.length)]);
            fault.setRepairCost(BigDecimal.valueOf(random.nextInt(5000) + 500));
            fault.setRepairDuration((double) (random.nextInt(48) + 1));
            
            LocalDateTime faultTime = LocalDateTime.now()
                .minusDays(random.nextInt(30))
                .minusHours(random.nextInt(24));
            fault.setFaultTime(faultTime);
            fault.setRepairTime(faultTime.plusHours(fault.getRepairDuration().longValue()));
            fault.setStatus("fixed");
            
            deviceFaultRepository.save(fault);
        }
    }

    /**
     * 初始化用户行为记录
     */
    private void initUserBehaviors() {
        String[] behaviorTypes = {"login", "browse", "add_to_cart", "purchase", "logout"};
        String[] descriptions = {
            "用户登录系统", "浏览商品列表", "将商品加入购物车", "完成订单购买", "用户退出登录"
        };

        for (int i = 0; i < 200; i++) {
            UserBehavior behavior = new UserBehavior();
            behavior.setUserId((long) (random.nextInt(50) + 1));
            
            int typeIndex = random.nextInt(behaviorTypes.length);
            behavior.setBehaviorType(behaviorTypes[typeIndex]);
            behavior.setDescription(descriptions[typeIndex]);
            
            if (typeIndex == 1 || typeIndex == 2 || typeIndex == 3) {
                behavior.setProductId((long) (random.nextInt(20) + 1));
            }
            
            LocalDateTime behaviorTime = LocalDateTime.now()
                .minusDays(random.nextInt(7))
                .withHour(random.nextInt(24))
                .withMinute(random.nextInt(60));
            behavior.setBehaviorTime(behaviorTime);
            
            userBehaviorRepository.save(behavior);
        }
    }
}
