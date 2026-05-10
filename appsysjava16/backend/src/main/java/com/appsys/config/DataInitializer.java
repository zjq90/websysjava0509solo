package com.appsys.config;

import com.appsys.entity.*;
import com.appsys.repository.*;
import com.appsys.util.AesEncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据初始化类
 * 在应用启动时自动初始化测试数据
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private SalesOrderRepository orderRepository;
    
    @Autowired
    private LogisticsTrackingRepository logisticsRepository;
    
    @Autowired
    private CustomerVisitRecordRepository visitRecordRepository;

    @Override
    public void run(String... args) {
        initProducts();
        initCustomers();
        initOrders();
    }

    /**
     * 初始化产品测试数据
     */
    private void initProducts() {
        if (productRepository.count() > 0) return;

        Product p1 = new Product();
        p1.setName("优质玉米种子A款");
        p1.setBatchNumber("2026AB01");
        p1.setSpecification("10kg/袋");
        p1.setExpiryDate(LocalDate.now().plusMonths(12));
        p1.setGerminationRate(new BigDecimal("95.5"));
        p1.setBasePrice(new BigDecimal("299.00"));
        p1.setStockQuantity(1000);
        p1.setDescription("优质玉米种子，适合北方种植，产量高，抗病性强");
        p1.setCategory("玉米种子");
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("高产小麦种子B款");
        p2.setBatchNumber("2026CD02");
        p2.setSpecification("25kg/袋");
        p2.setExpiryDate(LocalDate.now().plusMonths(18));
        p2.setGerminationRate(new BigDecimal("92.0"));
        p2.setBasePrice(new BigDecimal("499.00"));
        p2.setStockQuantity(500);
        p2.setDescription("高产小麦种子，抗倒伏，适合大面积种植");
        p2.setCategory("小麦种子");
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("优质水稻种子C款");
        p3.setBatchNumber("2026EF03");
        p3.setSpecification("5kg/袋");
        p3.setExpiryDate(LocalDate.now().plusMonths(24));
        p3.setGerminationRate(new BigDecimal("98.5"));
        p3.setBasePrice(new BigDecimal("199.00"));
        p3.setStockQuantity(2000);
        p3.setDescription("优质水稻种子，米质优，产量高");
        p3.setCategory("水稻种子");
        productRepository.save(p3);

        Product p4 = new Product();
        p4.setName("大豆种子D款");
        p4.setBatchNumber("2026GH04");
        p4.setSpecification("15kg/袋");
        p4.setExpiryDate(LocalDate.now().plusMonths(10));
        p4.setGerminationRate(new BigDecimal("90.5"));
        p4.setBasePrice(new BigDecimal("399.00"));
        p4.setStockQuantity(800);
        p4.setDescription("高蛋白大豆种子，出油率高");
        p4.setCategory("大豆种子");
        productRepository.save(p4);

        Product p5 = new Product();
        p5.setName("棉花种子E款");
        p5.setBatchNumber("2026IJ05");
        p5.setSpecification("1kg/袋");
        p5.setExpiryDate(LocalDate.now().plusMonths(8));
        p5.setGerminationRate(new BigDecimal("88.0"));
        p5.setBasePrice(new BigDecimal("599.00"));
        p5.setStockQuantity(300);
        p5.setDescription("优质棉花种子，纤维长，品质好");
        p5.setCategory("棉花种子");
        productRepository.save(p5);

        System.out.println("=== 产品测试数据初始化完成 ===");
    }

    /**
     * 初始化客户测试数据
     */
    private void initCustomers() {
        if (customerRepository.count() > 0) return;

        Customer c1 = new Customer();
        c1.setName("张三");
        c1.setPhone(AesEncryptionUtil.Encryptor.encrypt("13800138001"));
        c1.setLevel(CustomerLevel.VIP);
        c1.setCreditLimit(new BigDecimal("50000.00"));
        c1.setUsedCredit(new BigDecimal("15000.00"));
        c1.setTotalPurchaseAmount(new BigDecimal("35000.00"));
        c1.setAddress(AesEncryptionUtil.Encryptor.encrypt("北京市朝阳区建国路88号"));
        c1.setIsTemporary(false);
        c1.setRemarks("老客户，信誉良好");
        customerRepository.save(c1);

        Customer c2 = new Customer();
        c2.setName("李四");
        c2.setPhone(AesEncryptionUtil.Encryptor.encrypt("13800138002"));
        c2.setLevel(CustomerLevel.SVIP);
        c2.setCreditLimit(new BigDecimal("100000.00"));
        c2.setUsedCredit(new BigDecimal("30000.00"));
        c2.setTotalPurchaseAmount(new BigDecimal("85000.00"));
        c2.setAddress(AesEncryptionUtil.Encryptor.encrypt("上海市浦东新区世纪大道100号"));
        c2.setIsTemporary(false);
        c2.setRemarks("VIP客户，需要优先服务");
        customerRepository.save(c2);

        Customer c3 = new Customer();
        c3.setName("王五");
        c3.setPhone(AesEncryptionUtil.Encryptor.encrypt("13800138003"));
        c3.setLevel(CustomerLevel.NORMAL);
        c3.setCreditLimit(new BigDecimal("20000.00"));
        c3.setUsedCredit(new BigDecimal("5000.00"));
        c3.setTotalPurchaseAmount(new BigDecimal("12000.00"));
        c3.setAddress(AesEncryptionUtil.Encryptor.encrypt("广州市天河区天河路385号"));
        c3.setIsTemporary(false);
        customerRepository.save(c3);

        Customer c4 = new Customer();
        c4.setName("临时客户-展会");
        c4.setPhone(AesEncryptionUtil.Encryptor.encrypt("13900139001"));
        c4.setLevel(CustomerLevel.TEMPORARY);
        c4.setCreditLimit(BigDecimal.ZERO);
        c4.setUsedCredit(BigDecimal.ZERO);
        c4.setTotalPurchaseAmount(BigDecimal.ZERO);
        c4.setIsTemporary(true);
        c4.setRemarks("2026春季展会临时注册客户");
        customerRepository.save(c4);

        Customer c5 = new Customer();
        c5.setName("赵六");
        c5.setPhone(AesEncryptionUtil.Encryptor.encrypt("13800138005"));
        c5.setLevel(CustomerLevel.DIAMOND);
        c5.setCreditLimit(new BigDecimal("200000.00"));
        c5.setUsedCredit(new BigDecimal("80000.00"));
        c5.setTotalPurchaseAmount(new BigDecimal("150000.00"));
        c5.setAddress(AesEncryptionUtil.Encryptor.encrypt("深圳市南山区科技园南区"));
        c5.setIsTemporary(false);
        c5.setRemarks("钻石客户，长期合作伙伴");
        customerRepository.save(c5);

        System.out.println("=== 客户测试数据初始化完成 ===");
    }

    /**
     * 初始化订单和物流测试数据
     */
    private void initOrders() {
        if (orderRepository.count() > 0) return;

        SalesOrder o1 = new SalesOrder();
        o1.setOrderNo("SO202605100001");
        o1.setCustomerId(1L);
        o1.setStatus(OrderStatus.COMPLETED);
        o1.setOriginalAmount(new BigDecimal("2990.00"));
        o1.setDiscountAmount(new BigDecimal("299.00"));
        o1.setActualAmount(new BigDecimal("2691.00"));
        o1.setAppliedDiscountRate(new BigDecimal("0.90"));
        o1.setCustomerLevelAtOrder(CustomerLevel.VIP);
        o1.setSalespersonId(1L);
        o1.setSalespersonName("业务员小王");
        o1.setShippingAddress("北京市朝阳区建国路88号");
        o1.setReceiverPhone("13800138001");
        o1.setReceiverName("张三");
        o1.setContractNo("CT202605100001");
        o1.setSignedAt(LocalDateTime.now().minusDays(10));
        o1.setErpSyncedAt(LocalDateTime.now().minusDays(9));
        o1.setShippedAt(LocalDateTime.now().minusDays(7));
        o1.setDeliveredAt(LocalDateTime.now().minusDays(5));
        o1.setCreatedAt(LocalDateTime.now().minusDays(15));
        orderRepository.save(o1);

        SalesOrder o2 = new SalesOrder();
        o2.setOrderNo("SO202605100002");
        o2.setCustomerId(2L);
        o2.setStatus(OrderStatus.SHIPPED);
        o2.setOriginalAmount(new BigDecimal("4990.00"));
        o2.setDiscountAmount(new BigDecimal("748.50"));
        o2.setActualAmount(new BigDecimal("4241.50"));
        o2.setAppliedDiscountRate(new BigDecimal("0.85"));
        o2.setCustomerLevelAtOrder(CustomerLevel.SVIP);
        o2.setSalespersonId(1L);
        o2.setSalespersonName("业务员小王");
        o2.setShippingAddress("上海市浦东新区世纪大道100号");
        o2.setReceiverPhone("13800138002");
        o2.setReceiverName("李四");
        o2.setContractNo("CT202605100002");
        o2.setSignedAt(LocalDateTime.now().minusDays(5));
        o2.setErpSyncedAt(LocalDateTime.now().minusDays(4));
        o2.setShippedAt(LocalDateTime.now().minusDays(2));
        o2.setCreatedAt(LocalDateTime.now().minusDays(7));
        orderRepository.save(o2);

        SalesOrder o3 = new SalesOrder();
        o3.setOrderNo("SO202605100003");
        o3.setCustomerId(3L);
        o3.setStatus(OrderStatus.PENDING_SIGNATURE);
        o3.setOriginalAmount(new BigDecimal("1990.00"));
        o3.setDiscountAmount(new BigDecimal("99.50"));
        o3.setActualAmount(new BigDecimal("1890.50"));
        o3.setAppliedDiscountRate(new BigDecimal("0.95"));
        o3.setCustomerLevelAtOrder(CustomerLevel.NORMAL);
        o3.setSalespersonId(2L);
        o3.setSalespersonName("业务员小李");
        o3.setShippingAddress("广州市天河区天河路385号");
        o3.setReceiverPhone("13800138003");
        o3.setReceiverName("王五");
        o3.setCreatedAt(LocalDateTime.now().minusDays(1));
        orderRepository.save(o3);

        SalesOrder o4 = new SalesOrder();
        o4.setOrderNo("SO202605100004");
        o4.setCustomerId(5L);
        o4.setStatus(OrderStatus.STOCK_PREPARING);
        o4.setOriginalAmount(new BigDecimal("11980.00"));
        o4.setDiscountAmount(new BigDecimal("2396.00"));
        o4.setActualAmount(new BigDecimal("9584.00"));
        o4.setAppliedDiscountRate(new BigDecimal("0.80"));
        o4.setCustomerLevelAtOrder(CustomerLevel.DIAMOND);
        o4.setSalespersonId(1L);
        o4.setSalespersonName("业务员小王");
        o4.setShippingAddress("深圳市南山区科技园南区");
        o4.setReceiverPhone("13800138005");
        o4.setReceiverName("赵六");
        o4.setContractNo("CT202605100004");
        o4.setSignedAt(LocalDateTime.now().minusDays(1));
        o4.setErpSyncedAt(LocalDateTime.now().minusHours(12));
        o4.setCreatedAt(LocalDateTime.now().minusDays(2));
        orderRepository.save(o4);

        LogisticsTracking l1 = new LogisticsTracking();
        l1.setOrderId(1L);
        l1.setCompanyName("顺丰速运");
        l1.setCompanyCode("SF");
        l1.setTrackingNo("SF1000000001");
        l1.setStatus(LogisticsStatus.DELIVERED);
        l1.setCurrentLocation("【北京市】快件已签收，签收人：本人签收");
        l1.setShippedAt(LocalDateTime.now().minusDays(7));
        l1.setEstimatedDeliveryAt(LocalDateTime.now().minusDays(5));
        logisticsRepository.save(l1);

        LogisticsTracking l2 = new LogisticsTracking();
        l2.setOrderId(2L);
        l2.setCompanyName("顺丰速运");
        l2.setCompanyCode("SF");
        l2.setTrackingNo("SF1000000002");
        l2.setStatus(LogisticsStatus.IN_TRANSIT);
        l2.setCurrentLocation("【上海市】快件已到达【上海转运中心】");
        l2.setShippedAt(LocalDateTime.now().minusDays(2));
        l2.setEstimatedDeliveryAt(LocalDateTime.now().plusDays(2));
        logisticsRepository.save(l2);

        System.out.println("=== 订单和物流测试数据初始化完成 ===");

        CustomerVisitRecord v1 = new CustomerVisitRecord();
        v1.setCustomerId(1L);
        v1.setSalespersonId(1L);
        v1.setSalespersonName("业务员小王");
        v1.setVisitType("电话回访");
        v1.setVisitTime(LocalDateTime.now().minusDays(3));
        v1.setContent("回访客户，了解产品使用情况");
        v1.setFeedback("客户对产品满意，有复购意向");
        v1.setNextVisitTime(LocalDateTime.now().plusDays(30));
        visitRecordRepository.save(v1);

        CustomerVisitRecord v2 = new CustomerVisitRecord();
        v2.setCustomerId(2L);
        v2.setSalespersonId(1L);
        v2.setSalespersonName("业务员小王");
        v2.setVisitType("上门拜访");
        v2.setVisitTime(LocalDateTime.now().minusDays(5));
        v2.setContent("上门拜访，签订年度合作协议");
        v2.setFeedback("客户表示满意，将继续合作");
        visitRecordRepository.save(v2);

        System.out.println("=== 回访记录测试数据初始化完成 ===");
    }
}
