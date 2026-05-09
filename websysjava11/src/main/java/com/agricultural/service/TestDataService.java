package com.agricultural.service;

import com.agricultural.entity.*;
import com.agricultural.entity.enums.*;
import com.agricultural.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * 测试数据生成服务
 * 提供批量生成测试数据的功能，辅助完成系统功能测试
 */
@Service
public class TestDataService {

    private final VarietyRepository varietyRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final FinanceRepository financeRepository;
    private final CostRecordRepository costRecordRepository;

    private final Random random = new Random();

    public TestDataService(VarietyRepository varietyRepository,
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

    /**
     * 生成指定数量的销售订单
     */
    @Transactional
    public int generateSalesOrders(int count) {
        List<Variety> varieties = varietyRepository.findByEnabledTrue();
        List<Customer> customers = customerRepository.findByCustomerTypeAndEnabledTrue("CUSTOMER");
        
        if (varieties.isEmpty() || customers.isEmpty()) {
            return 0;
        }

        int generated = 0;
        for (int i = 0; i < count; i++) {
            try {
                Order order = createRandomOrder(OrderType.SALES, varieties, customers);
                orderRepository.save(order);
                generated++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return generated;
    }

    /**
     * 生成指定数量的采购订单
     */
    @Transactional
    public int generatePurchaseOrders(int count) {
        List<Variety> varieties = varietyRepository.findByEnabledTrue();
        List<Customer> suppliers = customerRepository.findByCustomerTypeAndEnabledTrue("SUPPLIER");
        
        if (varieties.isEmpty() || suppliers.isEmpty()) {
            return 0;
        }

        int generated = 0;
        for (int i = 0; i < count; i++) {
            try {
                Order order = createRandomOrder(OrderType.PURCHASE, varieties, suppliers);
                orderRepository.save(order);
                generated++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return generated;
    }

    /**
     * 生成指定数量的成本记录
     */
    @Transactional
    public int generateCostRecords(int count) {
        List<Variety> varieties = varietyRepository.findByEnabledTrue();
        List<Customer> suppliers = customerRepository.findByCustomerTypeAndEnabledTrue("SUPPLIER");
        
        if (varieties.isEmpty()) {
            return 0;
        }

        CostCategory[] categories = CostCategory.values();
        String[] expenseNames = {
            "种子采购", "化肥", "农药", "农机作业", "灌溉",
            "加工费", "包装材料", "运输费", "仓储费", "人工费"
        };
        String[] areas = {"东北基地", "华北基地", "华东基地", "华南基地", "西北基地"};
        String[] handlers = {"王场长", "李主管", "张农艺师", "陈技术员", "周物流"};

        int generated = 0;
        for (int i = 0; i < count; i++) {
            try {
                CostRecord record = new CostRecord();
                record.setCostNo("COST" + System.currentTimeMillis() + i);
                record.setCategory(categories[random.nextInt(categories.length)]);
                record.setVariety(varieties.get(random.nextInt(varieties.size())));
                record.setExpenseName(expenseNames[random.nextInt(expenseNames.length)]);
                record.setExpenseDate(LocalDate.now().minusDays(random.nextInt(90)));
                record.setQuantity(new BigDecimal(random.nextInt(1000) + 10));
                record.setUnit(new String[]{"公斤", "袋", "瓶", "车", "亩", "吨"}[random.nextInt(6)]);
                record.setUnitPrice(new BigDecimal(random.nextInt(500) + 10));
                record.setTotalAmount(record.getQuantity().multiply(record.getUnitPrice()));
                
                if (!suppliers.isEmpty() && random.nextBoolean()) {
                    record.setSupplier(suppliers.get(random.nextInt(suppliers.size())));
                }
                
                record.setArea(areas[random.nextInt(areas.length)]);
                record.setHandler(handlers[random.nextInt(handlers.length)]);
                record.setRemark("测试数据生成");
                
                costRecordRepository.save(record);
                generated++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return generated;
    }

    /**
     * 创建随机订单
     */
    private Order createRandomOrder(OrderType type, List<Variety> varieties, List<Customer> customers) {
        Order order = new Order();
        String prefix = type == OrderType.SALES ? "XS" : "CG";
        order.setOrderNo(prefix + System.currentTimeMillis());
        order.setOrderType(type);
        
        OrderStatus[] statuses = {OrderStatus.CONFIRMED, OrderStatus.SHIPPED, OrderStatus.COMPLETED};
        order.setOrderStatus(statuses[random.nextInt(statuses.length)]);
        
        order.setCustomer(customers.get(random.nextInt(customers.size())));
        order.setOrderDate(LocalDate.now().minusDays(random.nextInt(30)));
        order.setDeliveryDate(order.getOrderDate().plusDays(random.nextInt(7) + 1));
        order.setCreator("测试系统");
        order.setRemark("测试数据");

        int itemCount = random.nextInt(3) + 1;
        for (int i = 0; i < itemCount; i++) {
            OrderItem item = new OrderItem();
            item.setLineNo(i + 1);
            Variety variety = varieties.get(random.nextInt(varieties.size()));
            item.setVariety(variety);
            item.setQuantity(new BigDecimal(random.nextInt(1000) + 100));
            
            BigDecimal basePrice = variety.getStandardPrice() != null 
                ? variety.getStandardPrice() 
                : new BigDecimal("2.00");
            BigDecimal priceVariation = basePrice.multiply(new BigDecimal(0.1 + random.nextDouble() * 0.2));
            item.setUnitPrice(basePrice.add(random.nextBoolean() ? priceVariation : priceVariation.negate()));
            
            item.calculateLineTotal();
            item.setOrder(order);
            order.getOrderItems().add(item);
        }

        order.calculateAmounts();
        
        if (random.nextDouble() > 0.3) {
            order.setDiscountAmount(order.getTotalAmount().multiply(new BigDecimal(random.nextDouble() * 0.05)));
            order.calculateAmounts();
        }

        return order;
    }

    /**
     * 清空所有数据（谨慎使用）
     */
    @Transactional
    public void clearAllData() {
        costRecordRepository.deleteAll();
        orderRepository.deleteAll();
        financeRepository.deleteAll();
        customerRepository.deleteAll();
        varietyRepository.deleteAll();
    }
}
