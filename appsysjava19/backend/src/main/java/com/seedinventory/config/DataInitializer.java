package com.seedinventory.config;

import com.seedinventory.entity.Customer;
import com.seedinventory.entity.Inventory;
import com.seedinventory.entity.Seed;
import com.seedinventory.entity.Warehouse;
import com.seedinventory.repository.CustomerRepository;
import com.seedinventory.repository.InventoryRepository;
import com.seedinventory.repository.SeedRepository;
import com.seedinventory.repository.WarehouseRepository;
import com.seedinventory.util.AES256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 测试数据初始化类
 * 应用启动时自动加载测试数据
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private WarehouseRepository warehouseRepository;
    
    @Autowired
    private SeedRepository seedRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Value("${app.encryption.secret-key}")
    private String secretKey;
    
    @Value("${app.encryption.init-vector}")
    private String initVector;
    
    @Override
    public void run(String... args) throws Exception {
        initWarehouses();
        initSeeds();
        initInventory();
        initCustomers();
    }
    
    private void initWarehouses() {
        if (warehouseRepository.count() > 0) return;
        
        Warehouse w1 = new Warehouse();
        w1.setWarehouseCode("WH001");
        w1.setWarehouseName("北京中心仓库");
        w1.setAddress("北京市朝阳区仓储园区A座");
        w1.setManager("张三");
        w1.setPhone("010-12345678");
        w1.setStatus("ACTIVE");
        w1.setRemark("主仓库，恒温恒湿");
        
        Warehouse w2 = new Warehouse();
        w2.setWarehouseCode("WH002");
        w2.setWarehouseName("上海分仓");
        w2.setAddress("上海市浦东新区仓储中心B区");
        w2.setManager("李四");
        w2.setPhone("021-87654321");
        w2.setStatus("ACTIVE");
        w2.setRemark("华东地区配送中心");
        
        Warehouse w3 = new Warehouse();
        w3.setWarehouseCode("WH003");
        w3.setWarehouseName("广州冷藏仓");
        w3.setAddress("广州市白云区冷链物流园");
        w3.setManager("王五");
        w3.setPhone("020-11112222");
        w3.setStatus("ACTIVE");
        w3.setRemark("低温存储专用仓");
        
        warehouseRepository.save(w1);
        warehouseRepository.save(w2);
        warehouseRepository.save(w3);
    }
    
    private void initSeeds() {
        if (seedRepository.count() > 0) return;
        
        Seed s1 = new Seed();
        s1.setSeedCode("SD001");
        s1.setSeedName("京农1号小麦");
        s1.setCategory("粮食");
        s1.setVariety("冬小麦");
        s1.setSpecification("50kg/袋");
        s1.setUnit("kg");
        s1.setReferencePrice(new BigDecimal("12.50"));
        s1.setSupplier("中农集团");
        s1.setStatus("ACTIVE");
        
        Seed s2 = new Seed();
        s2.setSeedCode("SD002");
        s2.setSeedName("绿秀番茄");
        s2.setCategory("蔬菜");
        s2.setVariety("无限生长型");
        s2.setSpecification("1000粒/包");
        s2.setUnit("包");
        s2.setReferencePrice(new BigDecimal("25.00"));
        s2.setSupplier("北京蔬菜研究所");
        s2.setStatus("ACTIVE");
        
        Seed s3 = new Seed();
        s3.setSeedCode("SD003");
        s3.setSeedName("东北大豆");
        s3.setCategory("油料");
        s3.setVariety("高蛋白品种");
        s3.setSpecification("25kg/袋");
        s3.setUnit("kg");
        s3.setReferencePrice(new BigDecimal("8.80"));
        s3.setSupplier("黑龙江农垦");
        s3.setStatus("ACTIVE");
        
        Seed s4 = new Seed();
        s4.setSeedCode("SD004");
        s4.setSeedName("早佳8424西瓜");
        s4.setCategory("水果");
        s4.setVariety("早熟品种");
        s4.setSpecification("50g/袋");
        s4.setUnit("袋");
        s4.setReferencePrice(new BigDecimal("35.00"));
        s4.setSupplier("新疆农科院");
        s4.setStatus("ACTIVE");
        
        Seed s5 = new Seed();
        s5.setSeedCode("SD005");
        s5.setSeedName("德玉1号玉米");
        s5.setCategory("粮食");
        s5.setVariety("杂交品种");
        s5.setSpecification("2kg/袋");
        s5.setUnit("袋");
        s5.setReferencePrice(new BigDecimal("60.00"));
        s5.setSupplier("德国拜耳");
        s5.setStatus("ACTIVE");
        
        seedRepository.save(s1);
        seedRepository.save(s2);
        seedRepository.save(s3);
        seedRepository.save(s4);
        seedRepository.save(s5);
    }
    
    private void initInventory() {
        if (inventoryRepository.count() > 0) return;
        
        LocalDate today = LocalDate.now();
        
        Inventory i1 = new Inventory();
        i1.setBatchNo("WH01AB12");
        i1.setWarehouseId(1L);
        i1.setSeedId(1L);
        i1.setQuantity(new BigDecimal("5000"));
        i1.setUnit("kg");
        i1.setExpiryDate(today.plusMonths(12));
        i1.setGerminationRate(new BigDecimal("95.5"));
        i1.setOrigin("河北省");
        i1.setStorageLocation("A区-01-01");
        i1.setStatus("NORMAL");
        
        Inventory i2 = new Inventory();
        i2.setBatchNo("WH02CD34");
        i2.setWarehouseId(1L);
        i2.setSeedId(2L);
        i2.setQuantity(new BigDecimal("200"));
        i2.setUnit("包");
        i2.setExpiryDate(today.plusMonths(8));
        i2.setGerminationRate(new BigDecimal("90.0"));
        i2.setOrigin("山东省");
        i2.setStorageLocation("A区-02-05");
        i2.setStatus("NORMAL");
        
        Inventory i3 = new Inventory();
        i3.setBatchNo("WH03EF56");
        i3.setWarehouseId(2L);
        i3.setSeedId(3L);
        i3.setQuantity(new BigDecimal("3000"));
        i3.setUnit("kg");
        i3.setExpiryDate(today.plusMonths(18));
        i3.setGerminationRate(new BigDecimal("98.2"));
        i3.setOrigin("黑龙江");
        i3.setStorageLocation("B区-01-03");
        i3.setStatus("NORMAL");
        
        Inventory i4 = new Inventory();
        i4.setBatchNo("WH04GH78");
        i4.setWarehouseId(2L);
        i4.setSeedId(4L);
        i4.setQuantity(new BigDecimal("100"));
        i4.setUnit("袋");
        i4.setExpiryDate(today.plusMonths(7));
        i4.setGerminationRate(new BigDecimal("85.5"));
        i4.setOrigin("新疆");
        i4.setStorageLocation("B区-03-02");
        i4.setStatus("NORMAL");
        
        Inventory i5 = new Inventory();
        i5.setBatchNo("WH05IJ90");
        i5.setWarehouseId(3L);
        i5.setSeedId(5L);
        i5.setQuantity(new BigDecimal("50"));
        i5.setUnit("袋");
        i5.setExpiryDate(today.plusMonths(8));
        i5.setGerminationRate(new BigDecimal("92.0"));
        i5.setOrigin("甘肃");
        i5.setStorageLocation("C区-01-01");
        i5.setStatus("NORMAL");
        
        inventoryRepository.save(i1);
        inventoryRepository.save(i2);
        inventoryRepository.save(i3);
        inventoryRepository.save(i4);
        inventoryRepository.save(i5);
    }
    
    private void initCustomers() throws Exception {
        if (customerRepository.count() > 0) return;
        
        Customer c1 = new Customer();
        c1.setCustomerCode("C001");
        c1.setCustomerName("北京现代农业合作社");
        c1.setCustomerType("ENTERPRISE");
        c1.setContactName("刘经理");
        c1.setPhone(AES256Util.encrypt("13800138001", secretKey, initVector));
        c1.setEmail(AES256Util.encrypt("liujingli@bjfarm.com", secretKey, initVector));
        c1.setAddress(AES256Util.encrypt("北京市顺义区农业园区", secretKey, initVector));
        c1.setStatus("ACTIVE");
        
        Customer c2 = new Customer();
        c2.setCustomerCode("C002");
        c2.setCustomerName("上海果蔬种植基地");
        c2.setCustomerType("ENTERPRISE");
        c2.setContactName("王师傅");
        c2.setPhone(AES256Util.encrypt("13900139002", secretKey, initVector));
        c2.setEmail(AES256Util.encrypt("wangshifu@shveg.com", secretKey, initVector));
        c2.setAddress(AES256Util.encrypt("上海市青浦区蔬菜基地", secretKey, initVector));
        c2.setStatus("ACTIVE");
        
        Customer c3 = new Customer();
        c3.setCustomerCode("C003");
        c3.setCustomerName("广州种子经销部");
        c3.setCustomerType("INDIVIDUAL");
        c3.setContactName("陈老板");
        c3.setPhone(AES256Util.encrypt("13700137003", secretKey, initVector));
        c3.setAddress(AES256Util.encrypt("广州市天河区种子市场", secretKey, initVector));
        c3.setStatus("ACTIVE");
        
        customerRepository.save(c1);
        customerRepository.save(c2);
        customerRepository.save(c3);
    }
}
