package com.secondhand.config;

import com.secondhand.entity.*;
import com.secondhand.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 数据初始化类
 * 系统启动时自动加载测试数据
 *
 * @author secondhand
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PickupPointRepository pickupPointRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        initProducts();
        initPickupPoints();
        initCoupons();
        initTasks();

        System.out.println("测试数据初始化完成!");
    }

    private void initUsers() {
        if (userRepository.count() > 0) return;

        User user1 = new User();
        user1.setUsername("user1");
        user1.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        user1.setNickname("小明");
        user1.setAvatar("https://via.placeholder.com/150?text=User1");
        user1.setPhone("13800138001");
        user1.setGender("男");
        user1.setAge(25);
        user1.setLatitude(31.230416);
        user1.setLongitude(121.473701);
        user1.setAddress("上海市黄浦区人民广场");
        user1.setPoints(500);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("user2");
        user2.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        user2.setNickname("小红");
        user2.setAvatar("https://via.placeholder.com/150?text=User2");
        user2.setPhone("13800138002");
        user2.setGender("女");
        user2.setAge(23);
        user2.setLatitude(31.235416);
        user2.setLongitude(121.478701);
        user2.setAddress("上海市静安区南京西路");
        user2.setPoints(300);
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("seller1");
        user3.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        user3.setNickname("诚信卖家");
        user3.setAvatar("https://via.placeholder.com/150?text=Seller");
        user3.setPhone("13800138003");
        user3.setGender("男");
        user3.setAge(30);
        user3.setLatitude(31.220416);
        user3.setLongitude(121.463701);
        user3.setAddress("上海市徐汇区徐家汇");
        user3.setPoints(1000);
        userRepository.save(user3);
    }

    private void initProducts() {
        if (productRepository.count() > 0) return;

        Product p1 = new Product();
        p1.setUserId(3L);
        p1.setTitle("iPhone 13 Pro 256G 95新");
        p1.setDescription("自用iPhone 13 Pro，256G，无拆无修，功能完好，外观轻微划痕，配件齐全。");
        p1.setPrice(new BigDecimal("3999"));
        p1.setOriginalPrice(new BigDecimal("7999"));
        p1.setStock(1);
        p1.setCategory("手机数码");
        p1.setBrand("Apple");
        p1.setCondition("95新");
        p1.setImages("https://via.placeholder.com/400x300?text=iPhone13,https://via.placeholder.com/400x300?text=iPhone13-2");
        p1.setLatitude(31.220416);
        p1.setLongitude(121.463701);
        p1.setAddress("上海市徐汇区徐家汇地铁站");
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setUserId(3L);
        p2.setTitle("MacBook Pro 14寸 M1 Pro");
        p2.setDescription("2021款MacBook Pro 14寸，M1 Pro芯片，16G+512G，带Touch Bar，99新，带原装充电器。");
        p2.setPrice(new BigDecimal("8999"));
        p2.setOriginalPrice(new BigDecimal("14999"));
        p2.setStock(1);
        p2.setCategory("电脑办公");
        p2.setBrand("Apple");
        p2.setCondition("99新");
        p2.setImages("https://via.placeholder.com/400x300?text=MacBook");
        p2.setLatitude(31.220416);
        p2.setLongitude(121.463701);
        p2.setAddress("上海市徐汇区徐家汇地铁站");
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setUserId(2L);
        p3.setTitle("Nike Air Force 1 白色 42码");
        p3.setDescription("Nike经典款空军一号，只穿过2次，几乎全新，鞋盒都在。");
        p3.setPrice(new BigDecimal("399"));
        p3.setOriginalPrice(new BigDecimal("799"));
        p3.setStock(1);
        p3.setCategory("服装鞋帽");
        p3.setBrand("Nike");
        p3.setCondition("99新");
        p3.setImages("https://via.placeholder.com/400x300?text=Nike");
        p3.setLatitude(31.235416);
        p3.setLongitude(121.478701);
        p3.setAddress("上海市静安区南京西路");
        productRepository.save(p3);

        Product p4 = new Product();
        p4.setUserId(1L);
        p4.setTitle("小米空气净化器Pro H");
        p4.setDescription("小米空气净化器Pro H，除甲醛效果好，使用一年，滤芯刚换。");
        p4.setPrice(new BigDecimal("899"));
        p4.setOriginalPrice(new BigDecimal("1699"));
        p4.setStock(1);
        p4.setCategory("家用电器");
        p4.setBrand("小米");
        p4.setCondition("9成新");
        p4.setImages("https://via.placeholder.com/400x300?text=AirPurifier");
        p4.setLatitude(31.230416);
        p4.setLongitude(121.473701);
        p4.setAddress("上海市黄浦区人民广场");
        productRepository.save(p4);

        Product p5 = new Product();
        p5.setUserId(1L);
        p5.setTitle("索尼PS5 国行 光驱版");
        p5.setDescription("索尼PS5国行光驱版，带2个原装手柄，送3款游戏光盘，95新。");
        p5.setPrice(new BigDecimal("3299"));
        p5.setOriginalPrice(new BigDecimal("4299"));
        p5.setStock(1);
        p5.setCategory("手机数码");
        p5.setBrand("索尼");
        p5.setCondition("95新");
        p5.setImages("https://via.placeholder.com/400x300?text=PS5");
        p5.setLatitude(31.230416);
        p5.setLongitude(121.473701);
        p5.setAddress("上海市黄浦区人民广场");
        productRepository.save(p5);
    }

    private void initPickupPoints() {
        if (pickupPointRepository.count() > 0) return;

        PickupPoint pp1 = new PickupPoint();
        pp1.setName("人民广场自提点");
        pp1.setAddress("上海市黄浦区人民大道120号");
        pp1.setLatitude(31.230416);
        pp1.setLongitude(121.473701);
        pp1.setPhone("021-12345678");
        pp1.setBusinessHours("09:00-21:00");
        pickupPointRepository.save(pp1);

        PickupPoint pp2 = new PickupPoint();
        pp2.setName("徐家汇自提点");
        pp2.setAddress("上海市徐汇区漕溪北路88号");
        pp2.setLatitude(31.192020);
        pp2.setLongitude(121.431230);
        pp2.setPhone("021-87654321");
        pp2.setBusinessHours("08:30-22:00");
        pickupPointRepository.save(pp2);

        PickupPoint pp3 = new PickupPoint();
        pp3.setName("静安寺自提点");
        pp3.setAddress("上海市静安区南京西路1688号");
        pp3.setLatitude(31.224400);
        pp3.setLongitude(121.448200);
        pp3.setPhone("021-11112222");
        pp3.setBusinessHours("09:00-21:00");
        pickupPointRepository.save(pp3);
    }

    private void initCoupons() {
        if (couponRepository.count() > 0) return;

        Coupon c1 = new Coupon();
        c1.setName("新人专享优惠券");
        c1.setDescription("新用户注册即送，满100元可用");
        c1.setDiscountType("FIXED");
        c1.setDiscountValue(new BigDecimal("20"));
        c1.setMinAmount(new BigDecimal("100"));
        c1.setPointsRequired(0);
        c1.setTotalCount(1000);
        c1.setStatus("ACTIVE");
        couponRepository.save(c1);

        Coupon c2 = new Coupon();
        c2.setName("满500减50优惠券");
        c2.setDescription("满500元可用，全场通用");
        c2.setDiscountType("FIXED");
        c2.setDiscountValue(new BigDecimal("50"));
        c2.setMinAmount(new BigDecimal("500"));
        c2.setPointsRequired(100);
        c2.setTotalCount(500);
        c2.setStatus("ACTIVE");
        couponRepository.save(c2);

        Coupon c3 = new Coupon();
        c3.setName("满1000减100优惠券");
        c3.setDescription("满1000元可用，大额商品专用");
        c3.setDiscountType("FIXED");
        c3.setDiscountValue(new BigDecimal("100"));
        c3.setMinAmount(new BigDecimal("1000"));
        c3.setPointsRequired(200);
        c3.setTotalCount(200);
        c3.setStatus("ACTIVE");
        couponRepository.save(c3);
    }

    private void initTasks() {
        if (taskRepository.count() > 0) return;

        Task t1 = new Task();
        t1.setName("每日签到");
        t1.setDescription("每天签到获得积分奖励");
        t1.setType("DAILY");
        t1.setPoints(10);
        t1.setSortOrder(1);
        taskRepository.save(t1);

        Task t2 = new Task();
        t2.setName("首次发布商品");
        t2.setDescription("首次发布商品获得额外积分");
        t2.setType("ONCE");
        t2.setPoints(100);
        t2.setSortOrder(2);
        taskRepository.save(t2);

        Task t3 = new Task();
        t3.setName("完成首单");
        t3.setDescription("完成第一笔订单获得积分奖励");
        t3.setType("ONCE");
        t3.setPoints(50);
        t3.setSortOrder(3);
        taskRepository.save(t3);
    }

}
