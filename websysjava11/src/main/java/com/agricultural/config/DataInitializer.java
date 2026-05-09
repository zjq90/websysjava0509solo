package com.agricultural.config;

import com.agricultural.entity.*;
import com.agricultural.entity.enums.*;
import com.agricultural.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据初始化组件
 * 在应用启动时自动生成测试数据，包括：
 * - 品种信息（5个品种）
 * - 客户/供应商信息（8个）
 * - 订单数据（销售订单、采购订单）
 * - 财务账款（自动根据订单生成）
 * - 成本记录（多角度成本核算）
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final VarietyRepository varietyRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final FinanceRepository financeRepository;
    private final CostRecordRepository costRecordRepository;

    public DataInitializer(VarietyRepository varietyRepository,
                           CustomerRepository customerRepository,
                           OrderRepository orderRepository,
                           FinanceRepository financeRepository,
                           CostRecordRepository costRecordRepository) {
        this.varietyRepository = varietyRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.financeRepository = financeRepository;
        this.costRecordRepository = costRecordRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("正在初始化测试数据...");
        
        initVarieties();
        initCustomers();
        initOrders();
        initCostRecords();
        
        System.out.println("测试数据初始化完成！");
    }

    /**
     * 初始化品种数据
     */
    private void initVarieties() {
        String[][] varieties = {
                {"V001", "优质水稻-稻花香", "水稻", "公斤", "3.50", "2.00"},
                {"V002", "小麦新品种-济麦22", "小麦", "公斤", "2.80", "1.60"},
                {"V003", "玉米-登海605", "玉米", "公斤", "2.20", "1.20"},
                {"V004", "有机蔬菜-番茄", "蔬菜", "公斤", "5.00", "2.50"},
                {"V005", "有机蔬菜-黄瓜", "蔬菜", "公斤", "4.50", "2.20"}
        };

        for (String[] v : varieties) {
            Variety variety = new Variety();
            variety.setVarietyCode(v[0]);
            variety.setVarietyName(v[1]);
            variety.setCategory(v[2]);
            variety.setUnit(v[3]);
            variety.setStandardPrice(new BigDecimal(v[4]));
            variety.setStandardCost(new BigDecimal(v[5]));
            variety.setDescription(v[1] + " - 高品质农产品");
            variety.setEnabled(true);
            varietyRepository.save(variety);
        }
        System.out.println("已初始化品种数据: 5条");
    }

    /**
     * 初始化客户/供应商数据
     */
    private void initCustomers() {
        Object[][] customers = {
                {"C001", "北京市农产品批发中心", "CUSTOMER", "张三", "13800138001", "北京市", "北京市", "朝阳区", "100000"},
                {"C002", "上海市粮食购销公司", "CUSTOMER", "李四", "13800138002", "上海市", "上海市", "浦东新区", "200000"},
                {"C003", "广州市食品集团", "CUSTOMER", "王五", "13800138003", "广东省", "广州市", "天河区", "150000"},
                {"C004", "成都市农产品市场", "CUSTOMER", "赵六", "13800138004", "四川省", "成都市", "武侯区", "80000"},
                {"S001", "种子研发有限公司", "SUPPLIER", "钱七", "13900139001", "山东省", "济南市", "历下区", null},
                {"S002", "化肥农药供应商", "SUPPLIER", "孙八", "13900139002", "河南省", "郑州市", "金水区", null},
                {"S003", "物流公司", "SUPPLIER", "周九", "13900139003", "江苏省", "南京市", "鼓楼区", null},
                {"S004", "包装材料厂", "SUPPLIER", "吴十", "13900139004", "浙江省", "杭州市", "西湖区", null}
        };

        for (Object[] c : customers) {
            Customer customer = new Customer();
            customer.setCustomerCode((String) c[0]);
            customer.setCustomerName((String) c[1]);
            customer.setCustomerType((String) c[2]);
            customer.setContactPerson((String) c[3]);
            customer.setContactPhone((String) c[4]);
            customer.setProvince((String) c[5]);
            customer.setCity((String) c[6]);
            customer.setDistrict((String) c[7]);
            customer.setAddress(c[5] + "省" + c[6] + "市" + c[7]);
            if (c[8] != null) {
                customer.setCreditLimit(new BigDecimal((String) c[8]));
            }
            customer.setEnabled(true);
            customerRepository.save(customer);
        }
        System.out.println("已初始化客户/供应商数据: 8条");
    }

    /**
     * 初始化订单数据（含自动生成财务账款）
     */
    @Transactional
    public void initOrders() {
        Variety v1 = varietyRepository.findByVarietyCode("V001").orElse(null);
        Variety v2 = varietyRepository.findByVarietyCode("V002").orElse(null);
        Variety v3 = varietyRepository.findByVarietyCode("V003").orElse(null);
        Variety v4 = varietyRepository.findByVarietyCode("V004").orElse(null);
        
        Customer c1 = customerRepository.findByCustomerCode("C001").orElse(null);
        Customer c2 = customerRepository.findByCustomerCode("C002").orElse(null);
        Customer c3 = customerRepository.findByCustomerCode("C003").orElse(null);
        Customer s1 = customerRepository.findByCustomerCode("S001").orElse(null);
        Customer s2 = customerRepository.findByCustomerCode("S002").orElse(null);

        Order salesOrder1 = createOrder("XS20260501", OrderType.SALES, OrderStatus.CONFIRMED, c1,
                LocalDate.now().minusDays(5),
                new Object[][]{
                        {v1, new BigDecimal("5000"), new BigDecimal("3.50")},
                        {v2, new BigDecimal("3000"), new BigDecimal("2.80")}
                });
        createFinanceFromOrder(salesOrder1);

        Order salesOrder2 = createOrder("XS20260502", OrderType.SALES, OrderStatus.SHIPPED, c2,
                LocalDate.now().minusDays(3),
                new Object[][]{
                        {v1, new BigDecimal("8000"), new BigDecimal("3.60")},
                        {v3, new BigDecimal("5000"), new BigDecimal("2.25")}
                });
        createFinanceFromOrder(salesOrder2);

        Order salesOrder3 = createOrder("XS20260503", OrderType.SALES, OrderStatus.PENDING, c3,
                LocalDate.now().minusDays(1),
                new Object[][]{
                        {v4, new BigDecimal("2000"), new BigDecimal("5.00")}
                });

        Order purchaseOrder1 = createOrder("CG20260501", OrderType.PURCHASE, OrderStatus.CONFIRMED, s1,
                LocalDate.now().minusDays(7),
                new Object[][]{
                        {v1, new BigDecimal("10000"), new BigDecimal("2.00")},
                        {v2, new BigDecimal("8000"), new BigDecimal("1.60")}
                });
        createFinanceFromOrder(purchaseOrder1);

        Order purchaseOrder2 = createOrder("CG20260502", OrderType.PURCHASE, OrderStatus.COMPLETED, s2,
                LocalDate.now().minusDays(10),
                new Object[][]{
                        {v3, new BigDecimal("15000"), new BigDecimal("1.20")}
                });
        Finance finance2 = createFinanceFromOrder(purchaseOrder2);
        finance2.setPaidAmount(new BigDecimal("15000"));
        finance2.calculateRemainingAndStatus();
        financeRepository.save(finance2);

        System.out.println("已初始化订单数据: 5条");
    }

    private Order createOrder(String orderNo, OrderType orderType, OrderStatus status,
                              Customer customer, LocalDate orderDate, Object[][] items) {
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setOrderType(orderType);
        order.setOrderStatus(status);
        order.setCustomer(customer);
        order.setOrderDate(orderDate);
        order.setDeliveryDate(orderDate.plusDays(3));
        order.setCreator("系统管理员");
        order.setRemark("测试数据");

        for (int i = 0; i < items.length; i++) {
            OrderItem item = new OrderItem();
            item.setLineNo(i + 1);
            item.setVariety((Variety) items[i][0]);
            item.setQuantity((BigDecimal) items[i][1]);
            item.setUnitPrice((BigDecimal) items[i][2]);
            item.calculateLineTotal();
            item.setOrder(order);
            order.getOrderItems().add(item);
        }

        order.calculateAmounts();
        return orderRepository.save(order);
    }

    private Finance createFinanceFromOrder(Order order) {
        Finance finance = new Finance();
        String prefix = order.getOrderType() == OrderType.SALES ? "AR" : "AP";
        finance.setFinanceNo(prefix + order.getOrderNo().substring(2));
        finance.setFinanceType(order.getOrderType() == OrderType.SALES ? 
                FinanceType.RECEIVABLE : FinanceType.PAYABLE);
        finance.setStatus(FinanceStatus.UNSETTLED);
        finance.setCustomer(order.getCustomer());
        finance.setFinanceDate(order.getOrderDate());
        finance.setDueDate(order.getOrderDate().plusDays(30));
        finance.setTotalAmount(order.getNetAmount());
        finance.setPaidAmount(BigDecimal.ZERO);
        finance.setRemainingAmount(order.getNetAmount());
        finance.setSummary((order.getOrderType() == OrderType.SALES ? "销售" : "采购") + 
                "订单账款 - " + order.getOrderNo());
        finance.setOrder(order);
        finance = financeRepository.save(finance);
        
        order.setFinance(finance);
        orderRepository.save(order);
        
        return finance;
    }

    /**
     * 初始化成本记录（多角度成本核算）
     */
    private void initCostRecords() {
        Variety v1 = varietyRepository.findByVarietyCode("V001").orElse(null);
        Variety v2 = varietyRepository.findByVarietyCode("V002").orElse(null);
        Customer s1 = customerRepository.findByCustomerCode("S001").orElse(null);
        Customer s2 = customerRepository.findByCustomerCode("S002").orElse(null);
        Customer s3 = customerRepository.findByCustomerCode("S003").orElse(null);
        Customer s4 = customerRepository.findByCustomerCode("S004").orElse(null);

        CostRecord cost1 = new CostRecord();
        cost1.setCostNo("COST20260501");
        cost1.setCategory(CostCategory.BREEDING);
        cost1.setVariety(v1);
        cost1.setExpenseName("稻花香品种研发费用");
        cost1.setExpenseDate(LocalDate.now().minusDays(30));
        cost1.setQuantity(new BigDecimal("1"));
        cost1.setUnit("批");
        cost1.setUnitPrice(new BigDecimal("50000"));
        cost1.setTotalAmount(new BigDecimal("50000"));
        cost1.setSupplier(s1);
        cost1.setBatchNo("BATCH2026001");
        cost1.setArea("东北基地");
        cost1.setHandler("王技术员");
        costRecordRepository.save(cost1);

        CostRecord cost2 = new CostRecord();
        cost2.setCostNo("COST20260502");
        cost2.setCategory(CostCategory.FIELD_INPUT);
        cost2.setVariety(v1);
        cost2.setExpenseName("化肥采购-复合肥");
        cost2.setExpenseDate(LocalDate.now().minusDays(20));
        cost2.setQuantity(new BigDecimal("100"));
        cost2.setUnit("袋");
        cost2.setUnitPrice(new BigDecimal("180"));
        cost2.setTotalAmount(new BigDecimal("18000"));
        cost2.setSupplier(s2);
        cost2.setBatchNo("BATCH2026001");
        cost2.setArea("东北基地");
        cost2.setHandler("李场长");
        costRecordRepository.save(cost2);

        CostRecord cost3 = new CostRecord();
        cost3.setCostNo("COST20260503");
        cost3.setCategory(CostCategory.FIELD_INPUT);
        cost3.setVariety(v2);
        cost3.setExpenseName("农药采购-杀虫剂");
        cost3.setExpenseDate(LocalDate.now().minusDays(15));
        cost3.setQuantity(new BigDecimal("50"));
        cost3.setUnit("瓶");
        cost3.setUnitPrice(new BigDecimal("120"));
        cost3.setTotalAmount(new BigDecimal("6000"));
        cost3.setSupplier(s2);
        cost3.setBatchNo("BATCH2026002");
        cost3.setArea("华北基地");
        cost3.setHandler("张农艺师");
        costRecordRepository.save(cost3);

        CostRecord cost4 = new CostRecord();
        cost4.setCostNo("COST20260504");
        cost4.setCategory(CostCategory.PROCESSING);
        cost4.setExpenseName("稻谷加工费");
        cost4.setExpenseDate(LocalDate.now().minusDays(10));
        cost4.setQuantity(new BigDecimal("5000"));
        cost4.setUnit("公斤");
        cost4.setUnitPrice(new BigDecimal("0.50"));
        cost4.setTotalAmount(new BigDecimal("2500"));
        cost4.setArea("加工厂A");
        cost4.setHandler("陈主管");
        costRecordRepository.save(cost4);

        CostRecord cost5 = new CostRecord();
        cost5.setCostNo("COST20260505");
        cost5.setCategory(CostCategory.PACKAGING_LOGISTICS);
        cost5.setExpenseName("包装材料-编织袋");
        cost5.setExpenseDate(LocalDate.now().minusDays(8));
        cost5.setQuantity(new BigDecimal("2000"));
        cost5.setUnit("个");
        cost5.setUnitPrice(new BigDecimal("2.50"));
        cost5.setTotalAmount(new BigDecimal("5000"));
        cost5.setSupplier(s4);
        cost5.setHandler("周主管");
        costRecordRepository.save(cost5);

        CostRecord cost6 = new CostRecord();
        cost6.setCostNo("COST20260506");
        cost6.setCategory(CostCategory.PACKAGING_LOGISTICS);
        cost6.setExpenseName("运输费用-发往北京");
        cost6.setExpenseDate(LocalDate.now().minusDays(5));
        cost6.setQuantity(new BigDecimal("1"));
        cost6.setUnit("车");
        cost6.setUnitPrice(new BigDecimal("8000"));
        cost6.setTotalAmount(new BigDecimal("8000"));
        cost6.setSupplier(s3);
        cost6.setHandler("吴物流");
        costRecordRepository.save(cost6);

        CostRecord cost7 = new CostRecord();
        cost7.setCostNo("COST20260507");
        cost7.setCategory(CostCategory.ADMINISTRATIVE);
        cost7.setExpenseName("办公场地租金");
        cost7.setExpenseDate(LocalDate.now().withDayOfMonth(1));
        cost7.setQuantity(new BigDecimal("1"));
        cost7.setUnit("月");
        cost7.setUnitPrice(new BigDecimal("12000"));
        cost7.setTotalAmount(new BigDecimal("12000"));
        cost7.setHandler("行政部");
        costRecordRepository.save(cost7);

        System.out.println("已初始化成本记录: 7条");
    }
}
