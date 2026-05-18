package com.flower.config;

import com.flower.entity.*;
import com.flower.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据初始化类
 * 系统启动时自动生成测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void run(String... args) throws Exception {
        initProducts();
        initCustomers();
        initOrders();
        initAnnouncements();
        initArticles();
        System.out.println("========================================");
        System.out.println("测试数据初始化完成！");
        System.out.println("========================================");
    }

    private void initProducts() {
        String[] categories = {"玫瑰花", "康乃馨", "百合", "向日葵", "郁金香", "满天星"};
        String[] productNames = {
                "红玫瑰11支", "粉玫瑰19支", "白玫瑰99支", "粉色康乃馨",
                "多头百合", "向日葵花束", "紫色郁金香", "满天星干花"
        };
        double[] prices = {99.0, 159.0, 599.0, 79.0, 129.0, 89.0, 169.0, 49.0};
        int[] stocks = {100, 80, 20, 150, 60, 90, 8, 200};

        for (int i = 0; i < productNames.length; i++) {
            Product product = new Product();
            product.setName(productNames[i]);
            product.setPrice(BigDecimal.valueOf(prices[i]));
            product.setStock(stocks[i]);
            product.setMinStock(10);
            product.setDescription("精选优质" + productNames[i] + ",新鲜采摘,品质保证");
            product.setCategory(categories[i % categories.length]);
            product.setStatus(1);
            product.setCustomFlag(i < 2 ? 1 : 0);
            productRepository.save(product);
        }
    }

    private void initCustomers() {
        String[] names = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
        String[] phones = {"13800138001", "13800138002", "13800138003", "13800138004",
                           "13800138005", "13800138006", "13800138007", "13800138008"};
        String[] tags = {"高频客户", "企业客户", "VIP客户", "新客户",
                        "高频客户,VIP客户", "企业客户,新客户", "高频客户", ""};

        for (int i = 0; i < names.length; i++) {
            Customer customer = new Customer();
            customer.setName(names[i]);
            customer.setPhone(phones[i]);
            customer.setEmail("customer" + (i + 1) + "@example.com");
            customer.setGender(i % 2);
            customer.setTotalConsumption(BigDecimal.valueOf(100 * (i + 1)));
            customer.setOrderCount(i + 1);
            customer.setTags(tags[i]);
            customer.setLevel(Math.min(i / 2 + 1, 4));
            customer.setStatus(1);
            customerRepository.save(customer);
        }
    }

    private void initOrders() {
        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();

        String[] statuses = {"待审核", "备货中", "制作中", "已发货", "已完成", "已取消"};

        for (int i = 0; i < 10; i++) {
            Order order = new Order();
            order.setOrderNo("ORD" + (2024010000 + i));
            order.setCustomerId(customers.get(i % customers.size()).getId());
            order.setCustomerName(customers.get(i % customers.size()).getName());
            order.setCustomerPhone(customers.get(i % customers.size()).getPhone());
            order.setAddress("北京市朝阳区XX街道XX号");

            int itemCount = (i % 3) + 1;
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderItem> orderItems = new ArrayList<>();

            for (int j = 0; j < itemCount; j++) {
                Product product = products.get((i + j) % products.size());
                int quantity = j + 1;
                BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));

                OrderItem item = new OrderItem();
                item.setProductId(product.getId());
                item.setProductName(product.getName());
                item.setPrice(product.getPrice());
                item.setQuantity(quantity);
                item.setSubtotal(subtotal);
                orderItems.add(item);

                totalAmount = totalAmount.add(subtotal);
            }

            order.setTotalAmount(totalAmount);
            order.setStatus((i % 5) + 1);
            order.setCustomFlag(i < 3 ? 1 : 0);
            order.setPayStatus(i < 6 ? 1 : 0);
            order.setRemark("订单备注信息" + i);

            Order savedOrder = orderRepository.save(order);

            for (OrderItem item : orderItems) {
                item.setOrder(savedOrder);
                orderItemRepository.save(item);
            }
        }
    }

    private void initAnnouncements() {
        String[] titles = {
                "母亲节特惠活动开始啦！",
                "系统升级通知",
                "520情人节预订开启",
                "配送范围扩大通知"
        };
        String[] contents = {
                "母亲节期间,全场康乃馨8折优惠,满200元送精美花瓶一个！",
                "系统将于本周六凌晨2:00-4:00进行升级维护,期间可能影响部分功能使用。",
                "520情人节花束预订已开启,提前预订享7.5折优惠,数量有限先到先得！",
                "即日起配送范围扩大至周边5个区县,满199元免配送费。"
        };

        for (int i = 0; i < titles.length; i++) {
            Announcement announcement = new Announcement();
            announcement.setTitle(titles[i]);
            announcement.setContent(contents[i]);
            announcement.setType((i % 3) + 1);
            announcement.setTopFlag(i == 0 ? 1 : 0);
            announcement.setStatus(1);
            announcement.setPublishTime(LocalDateTime.now().minusDays(i));
            announcementRepository.save(announcement);
        }
    }

    private void initArticles() {
        String[] titles = {
                "玫瑰花的养护小知识",
                "不同颜色康乃馨的花语",
                "如何延长鲜花的保鲜期",
                "花艺入门：基础插花技巧",
                "各种场合送花指南"
        };
        String[] contents = {
                "玫瑰花养护要点：1.每天剪根换水；2.避免阳光直射；3.可添加少量营养液；4.去除浸泡在水中的叶子...",
                "红色康乃馨：祝你健康；粉色康乃馨：永远年轻美丽；白色康乃馨：纯洁的友谊；黄色康乃馨：感激之情...",
                "延长鲜花保鲜期的方法：1.使用干净的花瓶；2.温水浸泡根部；3.添加保鲜剂；4.每天喷水保持湿润...",
                "插花技巧入门：1.选择合适的花材搭配；2.高低错落有致；3.色彩协调统一；4.注意花材的朝向...",
                "送花指南：生日送玫瑰/百合；母亲节送康乃馨；情人节送红玫瑰；探望病人送康乃馨/向日葵..."
        };

        for (int i = 0; i < titles.length; i++) {
            Article article = new Article();
            article.setTitle(titles[i]);
            article.setContent(contents[i]);
            article.setCategory((i % 3) + 1);
            article.setSeoKeywords(titles[i] + ",鲜花,花艺");
            article.setSeoDescription(contents[i].substring(0, Math.min(50, contents[i].length())));
            article.setViewCount(100 * (i + 1));
            article.setStatus(1);
            article.setAuthor("花艺小助手");
            article.setPublishTime(LocalDateTime.now().minusDays(i));
            articleRepository.save(article);
        }
    }
}
