package com.vending.config;

import com.vending.entity.*;
import com.vending.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private VendingMachineRepository machineRepository;
    
    @Autowired
    private SlotRepository slotRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;
    
    @Override
    @Transactional
    public void run(String... args) {
        initCategories();
        initProducts();
        initMachines();
        initSlots();
        initOrders();
    }
    
    private void initCategories() {
        if (categoryRepository.count() > 0) return;
        
        List<Category> categories = new ArrayList<>();
        
        Category c1 = new Category();
        c1.setName("饮料");
        c1.setDescription("各类瓶装、罐装饮料");
        categories.add(c1);
        
        Category c2 = new Category();
        c2.setName("零食");
        c2.setDescription("休闲零食、膨化食品等");
        categories.add(c2);
        
        Category c3 = new Category();
        c3.setName("日用品");
        c3.setDescription("日常用品、小百货");
        categories.add(c3);
        
        categoryRepository.saveAll(categories);
    }
    
    private void initProducts() {
        if (productRepository.count() > 0) return;
        
        List<Category> categories = categoryRepository.findAll();
        Category drinkCat = categories.stream().filter(c -> "饮料".equals(c.getName())).findFirst().orElse(null);
        Category snackCat = categories.stream().filter(c -> "零食".equals(c.getName())).findFirst().orElse(null);
        Category dailyCat = categories.stream().filter(c -> "日用品".equals(c.getName())).findFirst().orElse(null);
        
        List<Product> products = new ArrayList<>();
        
        Product p1 = new Product();
        p1.setName("可口可乐 330ml");
        p1.setBarcode("6901234567890");
        p1.setSpecification("330ml/罐");
        p1.setCostPrice(new BigDecimal("1.50"));
        p1.setRetailPrice(new BigDecimal("3.00"));
        p1.setStockThreshold(3);
        p1.setCategory(drinkCat);
        p1.setActive(true);
        p1.setDescription("经典可口可乐，碳酸饮料");
        products.add(p1);
        
        Product p2 = new Product();
        p2.setName("农夫山泉 550ml");
        p2.setBarcode("6901234567891");
        p2.setSpecification("550ml/瓶");
        p2.setCostPrice(new BigDecimal("0.80"));
        p2.setRetailPrice(new BigDecimal("2.00"));
        p2.setStockThreshold(5);
        p2.setCategory(drinkCat);
        p2.setActive(true);
        p2.setDescription("天然矿泉水");
        products.add(p2);
        
        Product p3 = new Product();
        p3.setName("康师傅绿茶 500ml");
        p3.setBarcode("6901234567892");
        p3.setSpecification("500ml/瓶");
        p3.setCostPrice(new BigDecimal("1.20"));
        p3.setRetailPrice(new BigDecimal("3.50"));
        p3.setStockThreshold(3);
        p3.setCategory(drinkCat);
        p3.setActive(true);
        p3.setDescription("蜂蜜绿茶口味");
        products.add(p3);
        
        Product p4 = new Product();
        p4.setName("乐事薯片 原味");
        p4.setBarcode("6901234567893");
        p4.setSpecification("70g/袋");
        p4.setCostPrice(new BigDecimal("4.00"));
        p4.setRetailPrice(new BigDecimal("8.00"));
        p4.setStockThreshold(2);
        p4.setCategory(snackCat);
        p4.setActive(true);
        p4.setDescription("经典原味薯片");
        products.add(p4);
        
        Product p5 = new Product();
        p5.setName("奥利奥饼干");
        p5.setBarcode("6901234567894");
        p5.setSpecification("97g/盒");
        p5.setCostPrice(new BigDecimal("5.00"));
        p5.setRetailPrice(new BigDecimal("10.00"));
        p5.setStockThreshold(2);
        p5.setCategory(snackCat);
        p5.setActive(true);
        p5.setDescription("巧克力夹心饼干");
        products.add(p5);
        
        Product p6 = new Product();
        p6.setName("康师傅红烧牛肉面");
        p6.setBarcode("6901234567895");
        p6.setSpecification("105g/桶");
        p6.setCostPrice(new BigDecimal("3.00"));
        p6.setRetailPrice(new BigDecimal("5.00"));
        p6.setStockThreshold(3);
        p6.setCategory(snackCat);
        p6.setActive(true);
        p6.setDescription("桶装方便面");
        products.add(p6);
        
        Product p7 = new Product();
        p7.setName("心相印纸巾");
        p7.setBarcode("6901234567896");
        p7.setSpecification("10包/提");
        p7.setCostPrice(new BigDecimal("8.00"));
        p7.setRetailPrice(new BigDecimal("15.00"));
        p7.setStockThreshold(2);
        p7.setCategory(dailyCat);
        p7.setActive(true);
        p7.setDescription("软抽纸巾");
        products.add(p7);
        
        Product p8 = new Product();
        p8.setName("高露洁牙膏");
        p8.setBarcode("6901234567897");
        p8.setSpecification("140g/支");
        p8.setCostPrice(new BigDecimal("6.00"));
        p8.setRetailPrice(new BigDecimal("12.00"));
        p8.setStockThreshold(2);
        p8.setCategory(dailyCat);
        p8.setActive(true);
        p8.setDescription("草本牙膏");
        products.add(p8);
        
        productRepository.saveAll(products);
    }
    
    private void initMachines() {
        if (machineRepository.count() > 0) return;
        
        List<VendingMachine> machines = new ArrayList<>();
        
        VendingMachine m1 = new VendingMachine();
        m1.setMachineCode("VM001");
        m1.setName("A栋1楼售货机");
        m1.setLocation("科技园区A栋1楼大厅");
        m1.setIpAddress("192.168.1.101");
        m1.setSlotCount(20);
        m1.setStatus("ONLINE");
        m1.setLastOnlineTime(LocalDateTime.now());
        machines.add(m1);
        
        VendingMachine m2 = new VendingMachine();
        m2.setMachineCode("VM002");
        m2.setName("B栋2楼售货机");
        m2.setLocation("科技园区B栋2楼茶水间");
        m2.setIpAddress("192.168.1.102");
        m2.setSlotCount(20);
        m2.setStatus("ONLINE");
        m2.setLastOnlineTime(LocalDateTime.now());
        machines.add(m2);
        
        VendingMachine m3 = new VendingMachine();
        m3.setMachineCode("VM003");
        m3.setName("C栋负1楼售货机");
        m3.setLocation("科技园区C栋负1楼停车场");
        m3.setIpAddress("192.168.1.103");
        m3.setSlotCount(20);
        m3.setStatus("OFFLINE");
        machines.add(m3);
        
        machineRepository.saveAll(machines);
    }
    
    @Transactional
    public void initSlots() {
        List<VendingMachine> machines = machineRepository.findAll();
        List<Product> products = productRepository.findAll();
        
        for (VendingMachine machine : machines) {
            if (!slotRepository.findByMachineId(machine.getId()).isEmpty()) continue;
            
            for (int i = 1; i <= machine.getSlotCount(); i++) {
                Slot slot = new Slot();
                slot.setMachine(machine);
                slot.setSlotNumber(i);
                slot.setMaxCapacity(10);
                slot.setEnabled(true);
                
                if (i <= products.size() && !"OFFLINE".equals(machine.getStatus())) {
                    Product product = products.get(i - 1);
                    slot.setProduct(product);
                    int stock = (int) (Math.random() * 8) + 2;
                    slot.setCurrentStock(stock);
                } else {
                    slot.setCurrentStock(0);
                }
                
                slotRepository.save(slot);
            }
        }
    }
    
    @Transactional
    public void initOrders() {
        if (orderRepository.count() > 0) return;
        
        List<VendingMachine> machines = machineRepository.findAll();
        List<Product> products = productRepository.findAll();
        List<Slot> slots = slotRepository.findAll();
        
        if (machines.isEmpty() || products.isEmpty()) return;
        
        VendingMachine machine = machines.get(0);
        
        String[] paymentMethods = {"WECHAT", "ALIPAY"};
        String[] userNames = {"张三", "李四", "王五", "赵六", "钱七"};
        
        for (int i = 0; i < 15; i++) {
            Product product = products.get(i % products.size());
            Slot slot = slots.stream()
                .filter(s -> s.getProduct() != null && s.getProduct().getId().equals(product.getId())
                    && s.getMachine().getId().equals(machine.getId()))
                .findFirst().orElse(null);
            
            if (slot == null) continue;
            
            Order order = new Order();
            order.setOrderNo("ORD" + System.currentTimeMillis() + String.format("%02d", i));
            order.setMachine(machine);
            order.setUserIdentifier("USER" + (i % 5 + 1));
            order.setUserName(userNames[i % userNames.length]);
            order.setUserPhone("138" + String.format("%08d", 10000000 + i));
            
            int quantity = (int) (Math.random() * 2) + 1;
            BigDecimal total = product.getRetailPrice().multiply(BigDecimal.valueOf(quantity));
            order.setTotalAmount(total);
            order.setPaymentStatus(i < 12 ? "PAID" : (i < 14 ? "PENDING" : "CANCELLED"));
            order.setPickupStatus(i < 10 ? "PICKED" : (i < 12 ? "PENDING" : "CANCELLED"));
            order.setPaymentMethod(paymentMethods[i % paymentMethods.length]);
            order.setPaymentTransactionId("TXN" + System.currentTimeMillis() + i);
            
            LocalDateTime time = LocalDateTime.now().minusHours(i * 2);
            order.setCreateTime(time);
            if ("PAID".equals(order.getPaymentStatus())) {
                order.setPaymentTime(time.plusMinutes(1));
            }
            if ("PICKED".equals(order.getPickupStatus())) {
                order.setPickupTime(time.plusMinutes(2));
            }
            
            orderRepository.save(order);
            
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setProductName(product.getName());
            item.setPrice(product.getRetailPrice());
            item.setQuantity(quantity);
            item.setSubtotal(total);
            item.setSlot(slot);
            
            orderItemRepository.save(item);
        }
    }
}
