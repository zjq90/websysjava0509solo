package com.secondhand.config;

import com.secondhand.entity.Category;
import com.secondhand.entity.Product;
import com.secondhand.entity.User;
import com.secondhand.repository.CategoryRepository;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            initUsers();
        }
        if (categoryRepository.count() == 0) {
            initCategories();
        }
        if (productRepository.count() == 0) {
            initProducts();
        }
    }

    private void initUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        admin.setNickname("管理员");
        admin.setPhone("13800138000");
        admin.setEmail("admin@example.com");
        admin.setCreditScore(new BigDecimal("4.9"));
        admin.setLocation("北京·朝阳");
        admin.setStatus(1);
        admin.setIsVerified(true);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        userRepository.save(admin);

        User testUser = new User();
        testUser.setUsername("test");
        testUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        testUser.setNickname("测试用户");
        testUser.setPhone("13900139000");
        testUser.setEmail("test@example.com");
        testUser.setCreditScore(new BigDecimal("4.5"));
        testUser.setLocation("上海·浦东");
        testUser.setStatus(1);
        testUser.setIsVerified(false);
        testUser.setCreateTime(LocalDateTime.now());
        testUser.setUpdateTime(LocalDateTime.now());
        userRepository.save(testUser);

        System.out.println("测试用户已创建: admin/123456, test/123456");
    }

    private void initCategories() {
        Category phone = createCategory(null, "手机", "📱", 1, 1);
        Category computer = createCategory(null, "电脑", "💻", 2, 2);
        Category digital = createCategory(null, "数码", "📷", 3, 3);
        Category clothing = createCategory(null, "服饰", "👕", 4, 4);
        Category appliance = createCategory(null, "家电", "📺", 5, 5);
        Category book = createCategory(null, "图书", "📚", 6, 6);
        Category sports = createCategory(null, "运动", "⚽", 7, 7);
        Category baby = createCategory(null, "母婴", "🍼", 8, 8);

        categoryRepository.saveAll(Arrays.asList(phone, computer, digital, clothing, appliance, book, sports, baby));

        createCategory(phone.getId(), "iPhone", "📱", 1, 1);
        createCategory(phone.getId(), "华为", "📱", 2, 1);
        createCategory(phone.getId(), "小米", "📱", 3, 1);
        createCategory(phone.getId(), "OPPO", "📱", 4, 1);
        createCategory(phone.getId(), "vivo", "📱", 5, 1);

        createCategory(computer.getId(), "笔记本", "💻", 1, 2);
        createCategory(computer.getId(), "台式机", "💻", 2, 2);
        createCategory(computer.getId(), "平板电脑", "💻", 3, 2);

        createCategory(digital.getId(), "相机", "📷", 1, 3);
        createCategory(digital.getId(), "耳机", "🎧", 2, 3);
        createCategory(digital.getId(), "音箱", "🔊", 3, 3);

        System.out.println("分类数据已创建");
    }

    private Category createCategory(Long parentId, String name, String icon, int sort, int level) {
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(name);
        category.setIcon(icon);
        category.setSort(sort);
        category.setLevel(level);
        category.setStatus(1);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        return category;
    }

    private void initProducts() {
        String[] titles = {
            "iPhone 13 Pro 256G 石墨色 99新",
            "MacBook Pro 14寸 M1Pro 芯片",
            "华为Mate 40 Pro 8+256G",
            "小米12 Pro 骁龙8处理器",
            "iPad Air 5 256G 深空灰",
            "索尼A7M3 全画幅微单相机",
            "AirPods Pro 无线降噪耳机",
            "Nike Air Jordan 1 篮球鞋 42码",
            "戴森V10吸尘器 几乎全新",
            "Switch OLED 日版 带游戏卡带"
        };

        String[] descriptions = {
            "自用手机，保护很好，无磕碰，电池健康92%，所有功能正常。",
            "2021款MacBook Pro，M1Pro芯片，16G+512G，带充电器。",
            "华为旗舰手机，麒麟9000芯片，使用1年，成色95新。",
            "小米12 Pro，骁龙8 Gen1，12+256G，在保到明年。",
            "iPad Air 5代，M1芯片，256G，带Apple Pencil。",
            "索尼A7M3，快门1万次，带28-70镜头，配件齐全。",
            "AirPods Pro，降噪效果好，使用半年，充电盒有轻微划痕。",
            "AJ1经典配色，穿了几次，几乎全新，鞋盒都在。",
            "戴森V10吸尘器，吸力强劲，配件齐全，很少使用。",
            "Switch OLED日版，带塞尔达和马里奥奥德赛，包装齐全。"
        };

        BigDecimal[] prices = {
            new BigDecimal("4599"),
            new BigDecimal("9999"),
            new BigDecimal("3899"),
            new BigDecimal("2999"),
            new BigDecimal("3999"),
            new BigDecimal("7999"),
            new BigDecimal("899"),
            new BigDecimal("1299"),
            new BigDecimal("2499"),
            new BigDecimal("1999")
        };

        String[] conditions = {
            "99新", "95新", "95新", "9成新", "99新",
            "95新", "9成新", "95新", "99新", "95新"
        };

        String[] brands = {
            "苹果", "苹果", "华为", "小米", "苹果",
            "索尼", "苹果", "Nike", "戴森", "任天堂"
        };

        for (int i = 0; i < 10; i++) {
            Product product = new Product();
            product.setUserId(1L);
            product.setTitle(titles[i]);
            product.setDescription(descriptions[i]);
            product.setPrice(prices[i]);
            product.setOriginalPrice(prices[i].multiply(new BigDecimal("1.5")));
            product.setCategoryId((long) (i % 8 + 1));
            product.setBrand(brands[i]);
            product.setCondition(conditions[i]);
            product.setLocation("北京·朝阳");
            product.setStatus(1);
            product.setViewCount((int) (Math.random() * 1000));
            product.setFavoriteCount((int) (Math.random() * 100));
            product.setIsNegotiable(true);
            product.setIsDelivery(true);
            product.setIsPickup(true);
            product.setCreateTime(LocalDateTime.now().minusDays((long) (Math.random() * 30)));
            product.setUpdateTime(LocalDateTime.now());
            product.setRefreshTime(LocalDateTime.now());
            productRepository.save(product);
        }

        System.out.println("测试商品数据已创建，共10件商品");
    }
}
