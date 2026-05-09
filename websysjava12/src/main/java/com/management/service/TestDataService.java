package com.management.service;

import com.management.entity.*;
import com.management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 测试数据服务类
 * 用于生成测试数据，辅助功能测试
 */
@Service
@Transactional
public class TestDataService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private SaleItemRepository saleItemRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    private Random random = new Random();

    private final String[] categories = {"电子产品", "日用百货", "食品饮料", "服装鞋帽", "办公用品"};
    private final String[] productNames = {
        "苹果iPhone 15", "华为Mate 60", "小米14", "三星S24",
        "洗衣液", "洗洁精", "卫生纸", "牙膏",
        "矿泉水", "可乐", "牛奶", "面包",
        "T恤", "牛仔裤", "运动鞋", "外套",
        "笔记本", "钢笔", "文件夹", "打印纸"
    };

    private final String[] customerNames = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
    private final String[] supplierNames = {"优质供应商A", "优质供应商B", "优质供应商C", "优质供应商D", "优质供应商E"};

    /**
     * 生成所有测试数据
     */
    public Map<String, Object> generateAllTestData() {
        Map<String, Object> result = new LinkedHashMap<>();

        result.put("suppliers", generateSuppliers());
        result.put("customers", generateCustomers());
        result.put("products", generateProducts());
        result.put("purchases", generatePurchases());
        result.put("sales", generateSales());

        return result;
    }

    /**
     * 生成供应商测试数据
     */
    public int generateSuppliers() {
        supplierRepository.deleteAll();
        int count = 0;
        for (int i = 0; i < supplierNames.length; i++) {
            Supplier supplier = new Supplier();
            supplier.setSupplierName(supplierNames[i]);
            supplier.setContact("联系人" + (i + 1));
            supplier.setPhone("138" + String.format("%08d", random.nextInt(100000000)));
            supplier.setAddress("北京市朝阳区供应商路" + (i + 1) + "号");
            supplier.setEmail("supplier" + (i + 1) + "@example.com");
            supplierRepository.save(supplier);
            count++;
        }
        return count;
    }

    /**
     * 生成客户测试数据
     */
    public int generateCustomers() {
        customerRepository.deleteAll();
        int count = 0;
        String[] levels = {"普通客户", "VIP客户", "SVIP客户"};
        for (int i = 0; i < customerNames.length; i++) {
            Customer customer = new Customer();
            customer.setCustomerName(customerNames[i]);
            customer.setContact(customerNames[i]);
            customer.setPhone("139" + String.format("%08d", random.nextInt(100000000)));
            customer.setAddress("北京市海淀区客户街" + (i + 1) + "号");
            customer.setEmail("customer" + (i + 1) + "@example.com");
            customer.setLevel(levels[random.nextInt(levels.length)]);
            customerRepository.save(customer);
            count++;
        }
        return count;
    }

    /**
     * 生成产品测试数据
     */
    public int generateProducts() {
        inventoryRepository.deleteAll();
        productRepository.deleteAll();

        int count = 0;
        for (int i = 0; i < productNames.length; i++) {
            Product product = new Product();
            product.setProductName(productNames[i]);
            product.setCategory(categories[i / 4]);
            product.setDescription(productNames[i] + " - 优质产品");

            double costPrice = 50 + random.nextDouble() * 200;
            double salePrice = costPrice * (1.3 + random.nextDouble() * 0.5);

            product.setCostPrice(BigDecimal.valueOf(costPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setSalePrice(BigDecimal.valueOf(salePrice).setScale(2, BigDecimal.ROUND_HALF_UP));
            product.setUnit("件");
            product.setBarCode("BAR" + String.format("%08d", i + 1));

            Product savedProduct = productRepository.save(product);

            Inventory inventory = new Inventory();
            inventory.setProduct(savedProduct);
            int initialStock = 50 + random.nextInt(200);
            inventory.setQuantity(initialStock);
            inventory.setAvgCostPrice(product.getCostPrice());
            inventory.setTotalValue(product.getCostPrice().multiply(BigDecimal.valueOf(initialStock)));
            inventory.setMinStock(10);
            inventory.setMaxStock(500);
            inventoryRepository.save(inventory);

            count++;
        }
        return count;
    }

    /**
     * 生成采购测试数据
     */
    public int generatePurchases() {
        purchaseRepository.deleteAll();

        List<Supplier> suppliers = supplierRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (suppliers.isEmpty() || products.isEmpty()) {
            return 0;
        }

        int count = 0;
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 30; i++) {
            Purchase purchase = new Purchase();
            purchase.setPurchaseNo("CG" + System.currentTimeMillis() + random.nextInt(1000));
            purchase.setSupplier(suppliers.get(random.nextInt(suppliers.size())));

            Product product = products.get(random.nextInt(products.size()));
            purchase.setProduct(product);

            int quantity = 10 + random.nextInt(100);
            purchase.setQuantity(quantity);

            double priceVariation = 0.9 + random.nextDouble() * 0.2;
            purchase.setUnitPrice(product.getCostPrice().multiply(BigDecimal.valueOf(priceVariation)).setScale(2, BigDecimal.ROUND_HALF_UP));
            purchase.setTotalAmount(purchase.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));

            int daysAgo = random.nextInt(90);
            purchase.setPurchaseDate(now.minusDays(daysAgo));
            purchase.setStatus("已入库");
            purchase.setRemark("测试采购单-" + (i + 1));

            purchaseRepository.save(purchase);

            updateInventoryAfterPurchase(product.getId(), quantity, purchase.getUnitPrice());

            count++;
        }
        return count;
    }

    /**
     * 生成销售测试数据
     */
    public int generateSales() {
        saleItemRepository.deleteAll();
        saleRepository.deleteAll();

        List<Customer> customers = customerRepository.findAll();
        List<Product> products = productRepository.findAll();

        if (customers.isEmpty() || products.isEmpty()) {
            return 0;
        }

        int count = 0;
        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < 50; i++) {
            Sale sale = new Sale();
            sale.setSaleNo("XS" + System.currentTimeMillis() + random.nextInt(1000));
            sale.setCustomer(customers.get(random.nextInt(customers.size())));

            int daysAgo = random.nextInt(90);
            sale.setSaleDate(now.minusDays(daysAgo));
            sale.setStatus("已完成");
            sale.setRemark("测试销售单-" + (i + 1));

            Sale savedSale = saleRepository.save(sale);

            List<SaleItem> saleItems = new ArrayList<>();
            int itemCount = 1 + random.nextInt(5);
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalCost = BigDecimal.ZERO;

            Set<Long> usedProductIds = new HashSet<>();

            for (int j = 0; j < itemCount; j++) {
                Product product;
                do {
                    product = products.get(random.nextInt(products.size()));
                } while (usedProductIds.contains(product.getId()));
                usedProductIds.add(product.getId());

                SaleItem item = new SaleItem();
                item.setSale(savedSale);
                item.setProduct(product);

                int quantity = 1 + random.nextInt(20);
                item.setQuantity(quantity);

                item.setUnitPrice(product.getSalePrice());
                item.setCostPrice(product.getCostPrice());
                item.setAmount(product.getSalePrice().multiply(BigDecimal.valueOf(quantity)));
                item.setCost(product.getCostPrice().multiply(BigDecimal.valueOf(quantity)));
                item.setProfit(item.getAmount().subtract(item.getCost()));

                saleItemRepository.save(item);
                saleItems.add(item);

                totalAmount = totalAmount.add(item.getAmount());
                totalCost = totalCost.add(item.getCost());

                updateInventoryAfterSale(product.getId(), quantity);
            }

            savedSale.setTotalAmount(totalAmount);
            savedSale.setTotalCost(totalCost);
            savedSale.setProfit(totalAmount.subtract(totalCost));
            savedSale.setSaleItems(saleItems);
            saleRepository.save(savedSale);

            count++;
        }
        return count;
    }

    private void updateInventoryAfterPurchase(Long productId, int quantity, BigDecimal unitPrice) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            int oldQuantity = inventory.getQuantity();
            BigDecimal oldValue = inventory.getTotalValue() != null ? inventory.getTotalValue() : BigDecimal.ZERO;

            int newQuantity = oldQuantity + quantity;
            BigDecimal newValue = oldValue.add(unitPrice.multiply(BigDecimal.valueOf(quantity)));

            inventory.setQuantity(newQuantity);
            inventory.setTotalValue(newValue);
            if (newQuantity > 0) {
                inventory.setAvgCostPrice(newValue.divide(BigDecimal.valueOf(newQuantity), 2, BigDecimal.ROUND_HALF_UP));
            }
            inventory.setLastInTime(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }
    }

    private void updateInventoryAfterSale(Long productId, int quantity) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findByProductId(productId);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            int newQuantity = Math.max(0, inventory.getQuantity() - quantity);
            inventory.setQuantity(newQuantity);
            if (inventory.getAvgCostPrice() != null) {
                inventory.setTotalValue(inventory.getAvgCostPrice().multiply(BigDecimal.valueOf(newQuantity)));
            }
            inventory.setLastOutTime(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }
    }

    /**
     * 清除所有测试数据
     */
    public void clearAllTestData() {
        saleItemRepository.deleteAll();
        saleRepository.deleteAll();
        purchaseRepository.deleteAll();
        inventoryRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
        supplierRepository.deleteAll();
    }
}
