package com.flower.config;

import com.flower.entity.Coupon;
import com.flower.entity.Product;
import com.flower.entity.User;
import com.flower.repository.CouponRepository;
import com.flower.repository.ProductRepository;
import com.flower.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 数据初始化器
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public void run(String... args) throws Exception {
        initUsers();
        initProducts();
        initCoupons();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setNickname("管理员");
            admin.setPhone("13800138000");
            admin.setEmail("admin@flower.com");
            admin.setGender("男");
            admin.setAge(30);
            admin.setAddress("北京市朝阳区");
            admin.setPoints(1000);
            admin.setIsNewUser(false);
            userRepository.save(admin);

            User user = new User();
            user.setUsername("user");
            user.setPassword("user123");
            user.setNickname("测试用户");
            user.setPhone("13800138001");
            user.setEmail("user@flower.com");
            user.setGender("女");
            user.setAge(25);
            user.setAddress("上海市浦东新区");
            user.setPoints(500);
            user.setIsNewUser(true);
            userRepository.save(user);
        }
    }

    private void initProducts() {
        if (productRepository.count() == 0) {
            String[] categories = {"玫瑰", "百合", "康乃馨", "向日葵", "郁金香", "混搭花束"};
            String[][] products = {
                    {"红玫瑰11枝", "精选优质红玫瑰，搭配满天星、黄莺", "129.00", "99.00"},
                    {"粉玫瑰19枝", "粉色系玫瑰，浪漫温馨，适合送爱人", "199.00", "168.00"},
                    {"白玫瑰33枝", "纯洁白玫瑰，高端大气上档次", "299.00", "258.00"},
                    {"蓝色妖姬", "进口蓝玫瑰，神秘优雅", "399.00", "358.00"},
                    {"粉百合2枝", "粉色香水百合，清香怡人", "89.00", "68.00"},
                    {"白百合3枝", "纯洁白百合，祝福满满", "109.00", "88.00"},
                    {"康乃馨花束", "19枝粉色康乃馨，感恩母亲", "119.00", "98.00"},
                    {"向日葵花束", "6枝向日葵，阳光活力", "139.00", "118.00"},
                    {"郁金香花束", "10枝荷兰郁金香，高贵典雅", "259.00", "218.00"},
                    {"情人节专属", "99枝红玫瑰，浪漫求婚必备", "999.00", "888.00"},
                    {"生日祝福花束", "混搭鲜花，生日惊喜", "189.00", "158.00"},
                    {"毕业季花束", "向日葵+百合，前程似锦", "169.00", "138.00"}
            };

            for (int i = 0; i < products.length; i++) {
                Product product = new Product();
                product.setName(products[i][0]);
                product.setDescription(products[i][1]);
                product.setDetail(products[i][1] + "，精选优质花材，专业花艺师精心制作，同城配送最快2小时达。");
                product.setPrice(new BigDecimal(products[i][3]));
                product.setOriginalPrice(new BigDecimal(products[i][2]));
                product.setCategory(categories[i % categories.length]);
                product.setStock(100 + (int) (Math.random() * 200));
                product.setIsHot(i < 4);
                product.setIsNew(i >= 4 && i < 8);
                productRepository.save(product);
            }
        }
    }

    private void initCoupons() {
        if (couponRepository.count() == 0) {
            Coupon newUserCoupon = new Coupon();
            newUserCoupon.setName("新人专享券");
            newUserCoupon.setDescription("新用户注册即送，满50可用");
            newUserCoupon.setType("amount");
            newUserCoupon.setValue(new BigDecimal(20));
            newUserCoupon.setMinAmount(new BigDecimal(50));
            newUserCoupon.setTotalCount(1000);
            newUserCoupon.setIsNewUserOnly(true);
            newUserCoupon.setValidDays(30);
            couponRepository.save(newUserCoupon);

            Coupon coupon10 = new Coupon();
            coupon10.setName("满100减10");
            coupon10.setDescription("全场通用，满100元减10元");
            coupon10.setType("amount");
            coupon10.setValue(new BigDecimal(10));
            coupon10.setMinAmount(new BigDecimal(100));
            coupon10.setTotalCount(5000);
            coupon10.setIsNewUserOnly(false);
            coupon10.setValidDays(15);
            couponRepository.save(coupon10);

            Coupon coupon30 = new Coupon();
            coupon30.setName("满200减30");
            coupon30.setDescription("全场通用，满200元减30元");
            coupon30.setType("amount");
            coupon30.setValue(new BigDecimal(30));
            coupon30.setMinAmount(new BigDecimal(200));
            coupon30.setTotalCount(3000);
            coupon30.setIsNewUserOnly(false);
            coupon30.setValidDays(15);
            couponRepository.save(coupon30);

            Coupon coupon50 = new Coupon();
            coupon50.setName("满300减50");
            coupon50.setDescription("全场通用，满300元减50元");
            coupon50.setType("amount");
            coupon50.setValue(new BigDecimal(50));
            coupon50.setMinAmount(new BigDecimal(300));
            coupon50.setTotalCount(2000);
            coupon50.setIsNewUserOnly(false);
            coupon50.setValidDays(15);
            couponRepository.save(coupon50);
        }
    }
}