package com.flowerstore.backend.init;

import com.flowerstore.backend.entity.*;
import com.flowerstore.backend.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据初始化类
 * 系统启动时自动初始化测试数据
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MemberLevelRepository memberLevelRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductCategoryRelationRepository productCategoryRelationRepository;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Override
    public void run(String... args) {
        log.info("开始初始化系统数据...");

        initMemberLevels();
        initProductCategories();
        initProducts();
        initProductImages();
        initProductCategoryRelations();
        initProductReviews();
        initCoupons();

        log.info("系统数据初始化完成！");
    }

    /**
     * 初始化会员等级
     */
    private void initMemberLevels() {
        log.info("初始化会员等级数据...");
        List<MemberLevel> levels = new ArrayList<>();

        MemberLevel level1 = new MemberLevel();
        level1.setName("普通会员");
        level1.setUpgradeAmount(0L);
        level1.setDiscountRate(100);
        level1.setFreeShipping(0);
        level1.setPriorityPurchase(0);
        level1.setBirthdayDiscount(95);
        level1.setPointAccelerator(100);
        level1.setDescription("注册即成为普通会员，享受基础权益");
        level1.setSortOrder(1);
        level1.setStatus(1);
        level1.setCreateTime(LocalDateTime.now());
        levels.add(level1);

        MemberLevel level2 = new MemberLevel();
        level2.setName("VIP会员");
        level2.setUpgradeAmount(50000L);
        level2.setDiscountRate(90);
        level2.setFreeShipping(1);
        level2.setPriorityPurchase(0);
        level2.setBirthdayDiscount(85);
        level2.setPointAccelerator(150);
        level2.setDescription("累计消费满500元升级为VIP会员，享受9折优惠和免运费");
        level2.setSortOrder(2);
        level2.setStatus(1);
        level2.setCreateTime(LocalDateTime.now());
        levels.add(level2);

        MemberLevel level3 = new MemberLevel();
        level3.setName("高级VIP");
        level3.setUpgradeAmount(200000L);
        level3.setDiscountRate(80);
        level3.setFreeShipping(1);
        level3.setPriorityPurchase(1);
        level3.setBirthdayDiscount(85);
        level3.setPointAccelerator(200);
        level3.setDescription("累计消费满2000元升级为高级VIP，享受8折优惠、优先抢购权和专属客服");
        level3.setSortOrder(3);
        level3.setStatus(1);
        level3.setCreateTime(LocalDateTime.now());
        levels.add(level3);

        memberLevelRepository.saveAll(levels);
        log.info("会员等级数据初始化完成，共{}条", levels.size());
    }

    /**
     * 初始化产品分类
     */
    private void initProductCategories() {
        log.info("初始化产品分类数据...");
        List<ProductCategory> categories = new ArrayList<>();

        String[][] sceneCategories = {
                {"情人节专区", "浪漫情人节，为爱绽放"},
                {"生日鲜花", "生日快乐，美好相伴"},
                {"感恩致谢", "感恩的心，感谢有你"},
                {"婚礼用花", "浪漫婚礼，永恒见证"},
                {"探病慰问", "温馨祝福，早日康复"},
                {"商务用花", "高端大气，彰显品位"}
        };
        for (int i = 0; i < sceneCategories.length; i++) {
            ProductCategory category = new ProductCategory();
            category.setName(sceneCategories[i][0]);
            category.setDescription(sceneCategories[i][1]);
            category.setCategoryType("scene");
            category.setSortOrder(i + 1);
            category.setStatus(1);
            category.setCreateTime(LocalDateTime.now());
            category.setUpdateTime(LocalDateTime.now());
            categories.add(category);
        }

        String[][] materialCategories = {
                {"玫瑰", "爱情的象征，浪漫首选"},
                {"百合", "纯洁高雅，百年好合"},
                {"康乃馨", "温馨感恩，母爱之花"},
                {"向日葵", "阳光向上，充满活力"},
                {"郁金香", "高贵优雅，荷兰国花"},
                {"满天星", "点缀之美，不可或缺"}
        };
        for (int i = 0; i < materialCategories.length; i++) {
            ProductCategory category = new ProductCategory();
            category.setName(materialCategories[i][0]);
            category.setDescription(materialCategories[i][1]);
            category.setCategoryType("material");
            category.setSortOrder(i + 1);
            category.setStatus(1);
            category.setCreateTime(LocalDateTime.now());
            category.setUpdateTime(LocalDateTime.now());
            categories.add(category);
        }

        String[][] priceCategories = {
                {"0-100元", "实惠之选"},
                {"100-200元", "品质之选"},
                {"200-500元", "豪华之选"},
                {"500元以上", "尊享定制"}
        };
        for (int i = 0; i < priceCategories.length; i++) {
            ProductCategory category = new ProductCategory();
            category.setName(priceCategories[i][0]);
            category.setDescription(priceCategories[i][1]);
            category.setCategoryType("price");
            category.setSortOrder(i + 1);
            category.setStatus(1);
            category.setCreateTime(LocalDateTime.now());
            category.setUpdateTime(LocalDateTime.now());
            categories.add(category);
        }

        productCategoryRepository.saveAll(categories);
        log.info("产品分类数据初始化完成，共{}条", categories.size());
    }

    /**
     * 初始化产品数据
     */
    private void initProducts() {
        log.info("初始化产品数据...");
        List<Product> products = new ArrayList<>();

        String[][] productData = {
                {"红玫瑰浪漫花束", "11朵红玫瑰，搭配满天星", "https://picsum.photos/400/400?random=1", "9900", "12800", "玫瑰", "精美包装", "热恋、爱情", "情人节、生日"},
                {"粉色爱恋", "19朵粉玫瑰，浪漫温馨", "https://picsum.photos/400/400?random=2", "15800", "19800", "玫瑰", "粉色包装", "初恋、告白", "情人节、纪念日"},
                {"白色纯洁花束", "33朵白玫瑰，纯洁高雅", "https://picsum.photos/400/400?random=3", "26800", "32800", "玫瑰", "白色包装", "纯洁、尊敬", "婚礼、商务"},
                {"百合百年好合", "6朵香水百合，高雅清新", "https://picsum.photos/400/400?random=4", "18800", "22800", "百合", "绿色包装", "纯洁、百年好合", "婚礼、祝福"},
                {"康乃馨感恩花束", "20朵粉色康乃馨", "https://picsum.photos/400/400?random=5", "12800", "15800", "康乃馨", "粉色包装", "感恩、母爱", "母亲节、生日"},
                {"向日葵阳光花束", "9朵向日葵，阳光向上", "https://picsum.photos/400/400?random=6", "16800", "19800", "向日葵", "黄色包装", "阳光、活力", "生日、鼓励"},
                {"郁金香高贵花束", "15朵郁金香，优雅高贵", "https://picsum.photos/400/400?random=7", "21800", "25800", "郁金香", "紫色包装", "高贵、优雅", "商务、拜访"},
                {"满天星思念花束", "一大束满天星，满满思念", "https://picsum.photos/400/400?random=8", "8800", "10800", "满天星", "蓝色包装", "思念、青春", "毕业、友情"},
                {"99朵红玫瑰求婚花束", "99朵红玫瑰，天长地久", "https://picsum.photos/400/400?random=9", "88800", "99900", "玫瑰", "豪华包装", "求婚、爱情", "求婚、纪念日"},
                {"混合鲜花礼盒", "多种鲜花搭配，精美礼盒", "https://picsum.photos/400/400?random=10", "29800", "35800", "玫瑰,百合", "精美礼盒", "多样、精美", "各种场合"},
                {"永生花礼盒", "永不凋谢的玫瑰花", "https://picsum.photos/400/400?random=11", "59800", "69800", "玫瑰", "玻璃礼盒", "永恒、珍藏", "纪念日、收藏"},
                {"情人节限定花束", "情人节专属定制，限量发售", "https://picsum.photos/400/400?random=12", "39800", "49800", "玫瑰", "情人节限定", "浪漫、专属", "情人节"}
        };

        for (int i = 0; i < productData.length; i++) {
            String[] data = productData[i];
            Product product = new Product();
            product.setName(data[0]);
            product.setSubtitle(data[1]);
            product.setMainImage(data[2]);
            product.setPrice(Long.parseLong(data[3]));
            product.setOriginalPrice(Long.parseLong(data[4]));
            product.setFlowerMaterial(data[5]);
            product.setPackaging(data[6]);
            product.setFlowerLanguage(data[7]);
            product.setApplicableScene(data[8]);
            product.setCareGuide("1. 收到鲜花后，立即剪根2-3cm，45度斜剪\n2. 插入清水中，水位约10-15cm\n3. 每天换水一次，保持水质清洁\n4. 避免阳光直射和空调出风口\n5. 室内温度保持18-25度最佳");
            product.setDetail("<h3>产品详情</h3><p>精选优质鲜花，专业花艺师精心搭配，每一束都是艺术品。</p>" +
                    "<h3>配送说明</h3><p>全国主要城市当日达，专业配送团队，确保鲜花完好送达。</p>" +
                    "<h3>售后服务</h3><p>如有任何问题，请联系客服，我们将竭诚为您服务。");
            product.setStock(100);
            product.setSales(50 + i * 10);
            product.setIsHot(i < 4 ? 1 : 0);
            product.setIsNew(i < 3 ? 1 : 0);
            product.setIsRecommend(i < 5 ? 1 : 0);
            product.setPointExchange(0);
            product.setSortOrder(i + 1);
            product.setStatus(1);
            product.setCreateTime(LocalDateTime.now());
            product.setUpdateTime(LocalDateTime.now());
            products.add(product);
        }

        productRepository.saveAll(products);
        log.info("产品数据初始化完成，共{}条", products.size());
    }

    /**
     * 初始化产品图片
     */
    private void initProductImages() {
        log.info("初始化产品图片数据...");
        List<ProductImage> images = new ArrayList<>();

        for (long productId = 1; productId <= 12; productId++) {
            for (int i = 1; i <= 5; i++) {
                ProductImage image = new ProductImage();
                image.setProductId(productId);
                image.setImageUrl("https://picsum.photos/800/800?random=" + (productId * 10 + i));
                image.setType(i == 1 ? 1 : 2);
                image.setSortOrder(i);
                image.setCreateTime(LocalDateTime.now());
                images.add(image);
            }
        }

        productImageRepository.saveAll(images);
        log.info("产品图片数据初始化完成，共{}条", images.size());
    }

    /**
     * 初始化产品分类关联
     */
    private void initProductCategoryRelations() {
        log.info("初始化产品分类关联数据...");
        List<ProductCategoryRelation> relations = new ArrayList<>();

        for (long productId = 1; productId <= 12; productId++) {
            ProductCategoryRelation relation1 = new ProductCategoryRelation();
            relation1.setProductId(productId);
            relation1.setCategoryId((productId % 6) + 1);
            relations.add(relation1);

            ProductCategoryRelation relation2 = new ProductCategoryRelation();
            relation2.setProductId(productId);
            relation2.setCategoryId(6 + ((productId - 1) % 6) + 1);
            relations.add(relation2);

            ProductCategoryRelation relation3 = new ProductCategoryRelation();
            relation3.setProductId(productId);
            relation3.setCategoryId(12 + ((productId - 1) % 4) + 1);
            relations.add(relation3);
        }

        productCategoryRelationRepository.saveAll(relations);
        log.info("产品分类关联数据初始化完成，共{}条", relations.size());
    }

    /**
     * 初始化产品评价
     */
    private void initProductReviews() {
        log.info("初始化产品评价数据...");
        List<ProductReview> reviews = new ArrayList<>();

        String[][] reviewData = {
                {"花很新鲜，配送很快，女朋友很喜欢！", "5"},
                {"包装精美，鲜花质量很好，推荐购买！", "5"},
                {"性价比很高，下次还会回购！", "5"},
                {"客服态度很好，有问必答，好评！", "5"},
                {"花束比图片还好看，惊喜！", "5"}
        };

        for (long productId = 1; productId <= 12; productId++) {
            for (int i = 0; i < reviewData.length; i++) {
                ProductReview review = new ProductReview();
                review.setProductId(productId);
                review.setUserId((i + 1) * 1L);
                review.setRating(Integer.parseInt(reviewData[i][1]));
                review.setContent(reviewData[i][0]);
                review.setIsAnonymous(1);
                review.setLikeCount(10 + i);
                review.setStatus(1);
                review.setCreateTime(LocalDateTime.now().minusDays(i));
                reviews.add(review);
            }
        }

        productReviewRepository.saveAll(reviews);
        log.info("产品评价数据初始化完成，共{}条", reviews.size());
    }

    /**
     * 初始化优惠券
     */
    private void initCoupons() {
        log.info("初始化优惠券数据...");
        List<Coupon> coupons = new ArrayList<>();

        Coupon coupon1 = new Coupon();
        coupon1.setName("新用户专享券");
        coupon1.setDescription("新用户注册即送，满50可用");
        coupon1.setType(1);
        coupon1.setValue(2000L);
        coupon1.setMinAmount(5000L);
        coupon1.setTotalCount(1000);
        coupon1.setReceiveCount(0);
        coupon1.setUsedCount(0);
        coupon1.setPerLimit(1);
        coupon1.setPointExchange(0);
        coupon1.setStatus(1);
        coupon1.setCreateTime(LocalDateTime.now());
        coupons.add(coupon1);

        Coupon coupon2 = new Coupon();
        coupon2.setName("满100减15");
        coupon2.setDescription("全场通用，满100元减15元");
        coupon2.setType(1);
        coupon2.setValue(1500L);
        coupon2.setMinAmount(10000L);
        coupon2.setTotalCount(5000);
        coupon2.setReceiveCount(0);
        coupon2.setUsedCount(0);
        coupon2.setPerLimit(3);
        coupon2.setPointExchange(1);
        coupon2.setExchangePoints(100);
        coupon2.setStatus(1);
        coupon2.setCreateTime(LocalDateTime.now());
        coupons.add(coupon2);

        Coupon coupon3 = new Coupon();
        coupon3.setName("会员9折券");
        coupon3.setDescription("VIP会员专享，全场9折");
        coupon3.setType(2);
        coupon3.setDiscountRate(90);
        coupon3.setMinAmount(0L);
        coupon3.setTotalCount(1000);
        coupon3.setReceiveCount(0);
        coupon3.setUsedCount(0);
        coupon3.setPerLimit(1);
        coupon3.setPointExchange(0);
        coupon3.setStatus(1);
        coupon3.setCreateTime(LocalDateTime.now());
        coupons.add(coupon3);

        Coupon coupon4 = new Coupon();
        coupon4.setName("免运费券");
        coupon4.setDescription("全场免运费，无门槛");
        coupon4.setType(3);
        coupon4.setMinAmount(0L);
        coupon4.setTotalCount(2000);
        coupon4.setReceiveCount(0);
        coupon4.setUsedCount(0);
        coupon4.setPerLimit(5);
        coupon4.setPointExchange(1);
        coupon4.setExchangePoints(50);
        coupon4.setStatus(1);
        coupon4.setCreateTime(LocalDateTime.now());
        coupons.add(coupon4);

        couponRepository.saveAll(coupons);
        log.info("优惠券数据初始化完成，共{}条", coupons.size());
    }
}
