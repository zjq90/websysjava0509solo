package com.sales.config;

import com.sales.entity.*;
import com.sales.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 测试数据初始化类
 * 在应用启动时自动生成测试数据
 */
@Component
public class TestDataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PriceStrategyRepository priceStrategyRepository;

    @Override
    public void run(String... args) throws Exception {
        // 只有在数据为空时才生成测试数据
        if (productRepository.count() == 0) {
            initProducts();
        }
        if (customerRepository.count() == 0) {
            initCustomers();
        }
        if (priceStrategyRepository.count() == 0) {
            initPriceStrategies();
        }
    }

    /**
     * 初始化产品数据
     */
    private void initProducts() {
        String[][] productData = {
            {"P001", "尿素46%", "化肥", "50kg/袋", "2800.00", "1000", "吨"},
            {"P002", "复合肥NPK", "化肥", "40kg/袋", "3200.00", "800", "吨"},
            {"P003", "磷酸二铵", "化肥", "50kg/袋", "3500.00", "500", "吨"},
            {"P004", "有机肥料", "化肥", "25kg/袋", "1800.00", "300", "吨"},
            {"P005", "杀虫剂", "农药", "1L/瓶", "80.00", "2000", "箱"},
            {"P006", "杀菌剂", "农药", "500g/瓶", "120.00", "1500", "箱"},
            {"P007", "除草剂", "农药", "1L/瓶", "65.00", "1800", "箱"},
            {"P008", "叶面肥", "化肥", "20L/桶", "280.00", "600", "桶"}
        };

        for (String[] data : productData) {
            Product product = new Product();
            product.setCode(data[0]);
            product.setName(data[1]);
            product.setCategory(data[2]);
            product.setSpecification(data[3]);
            product.setBasePrice(new BigDecimal(data[4]));
            product.setStockQuantity(Integer.parseInt(data[5]));
            product.setUnit(data[6]);
            product.setActive(true);
            productRepository.save(product);
        }
    }

    /**
     * 初始化客户数据
     */
    private void initCustomers() {
        Object[][] customerData = {
            {"C001", "张家庄种植合作社", CustomerType.COOPERATIVE, CustomerLevel.CORE, "张三", "13800138001", "河北省", "石家庄市", "张家庄镇", "重要客户,长期合作"},
            {"C002", "李大户农场", CustomerType.FARMER, CustomerLevel.GOOD, "李四", "13800138002", "山东省", "济南市", "李村镇", "种植大户"},
            {"C003", "王记农资经销部", CustomerType.DISTRIBUTOR, CustomerLevel.GOOD, "王五", "13800138003", "河南省", "郑州市", "农业路123号", "经销商,优质客户"},
            {"C004", "阳光家庭农场", CustomerType.FARMER, CustomerLevel.NORMAL, "赵六", "13800138004", "江苏省", "南京市", "阳光镇", ""},
            {"C005", "丰收农机合作社", CustomerType.COOPERATIVE, CustomerLevel.GOOD, "钱七", "13800138005", "安徽省", "合肥市", "丰收路88号", "合作社,农机服务"},
            {"C006", "绿源农资公司", CustomerType.DISTRIBUTOR, CustomerLevel.CORE, "孙八", "13800138006", "浙江省", "杭州市", "绿源大厦", "核心经销商,VIP"},
            {"C007", "周农户", CustomerType.FARMER, CustomerLevel.NORMAL, "周九", "13800138007", "湖南省", "长沙市", "周家村", ""},
            {"C008", "新农民联合社", CustomerType.COOPERATIVE, CustomerLevel.NORMAL, "吴十", "13800138008", "湖北省", "武汉市", "新农民路", "新客户"}
        };

        for (Object[] data : customerData) {
            Customer customer = new Customer();
            customer.setCode((String) data[0]);
            customer.setName((String) data[1]);
            customer.setType((CustomerType) data[2]);
            customer.setLevel((CustomerLevel) data[3]);
            customer.setContactPerson((String) data[4]);
            customer.setPhone((String) data[5]);
            customer.setProvince((String) data[6]);
            customer.setCity((String) data[7]);
            customer.setAddress((String) data[8]);
            customer.setTags((String) data[9]);
            customer.setActive(true);
            customerRepository.save(customer);
        }
    }

    /**
     * 初始化价格策略数据
     */
    private void initPriceStrategies() {
        // 策略1：核心客户9折优惠
        PriceStrategy s1 = new PriceStrategy();
        s1.setName("核心客户折扣");
        s1.setCustomerLevel(CustomerLevel.CORE);
        s1.setPriceType(PriceType.DISCOUNT);
        s1.setDiscountRate(new BigDecimal("0.90"));
        s1.setPriority(100);
        s1.setActive(true);
        s1.setDescription("所有核心客户享受9折优惠");
        priceStrategyRepository.save(s1);

        // 策略2：优质客户95折优惠
        PriceStrategy s2 = new PriceStrategy();
        s2.setName("优质客户折扣");
        s2.setCustomerLevel(CustomerLevel.GOOD);
        s2.setPriceType(PriceType.DISCOUNT);
        s2.setDiscountRate(new BigDecimal("0.95"));
        s2.setPriority(90);
        s2.setActive(true);
        s2.setDescription("所有优质客户享受95折优惠");
        priceStrategyRepository.save(s2);

        // 策略3：经销商类型固定价格
        PriceStrategy s3 = new PriceStrategy();
        s3.setName("经销商尿素特价");
        s3.setProduct(productRepository.findByCode("P001").orElse(null));
        s3.setCustomerType(CustomerType.DISTRIBUTOR);
        s3.setPriceType(PriceType.FIXED);
        s3.setFixedPrice(new BigDecimal("2500.00"));
        s3.setPriority(95);
        s3.setActive(true);
        s3.setDescription("经销商购买尿素享受特价2500元/吨");
        priceStrategyRepository.save(s3);

        // 策略4：浙江省区域优惠
        PriceStrategy s4 = new PriceStrategy();
        s4.setName("浙江省区域优惠");
        s4.setRegion("浙江省");
        s4.setPriceType(PriceType.DISCOUNT);
        s4.setDiscountRate(new BigDecimal("0.92"));
        s4.setPriority(85);
        s4.setActive(true);
        s4.setDescription("浙江省客户额外享受8折优惠（与其他折扣叠加）");
        priceStrategyRepository.save(s4);

        // 策略5：批量优惠（100吨以上9折）
        PriceStrategy s5 = new PriceStrategy();
        s5.setName("批量采购优惠");
        s5.setMinQuantity(100);
        s5.setPriceType(PriceType.DISCOUNT);
        s5.setDiscountRate(new BigDecimal("0.88"));
        s5.setPriority(80);
        s5.setActive(true);
        s5.setDescription("单次采购100吨以上享受88折优惠");
        priceStrategyRepository.save(s5);
    }
}
