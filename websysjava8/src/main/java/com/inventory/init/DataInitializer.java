package com.inventory.init;

import com.inventory.entity.*;
import com.inventory.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据初始化组件
 * 系统启动时自动初始化测试数据
 * 包括：品类、品种、仓库、门店、种子批次、库存、环境记录等
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private VarietyRepository varietyRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private SeedBatchRepository seedBatchRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private EnvRecordRepository envRecordRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(String... args) throws Exception {
        // 检查是否已有数据，避免重复初始化
        if (categoryRepository.count() > 0) {
            System.out.println("系统已有数据，跳过初始化...");
            return;
        }

        System.out.println("开始初始化测试数据...");

        initCategories();
        initVarieties();
        initWarehouses();
        initStores();
        initSeedBatches();
        initInventory();
        initEnvRecords();

        System.out.println("测试数据初始化完成！");
    }

    /**
     * 初始化品类数据
     */
    private void initCategories() {
        System.out.println("初始化品类数据...");
        
        String[][] categories = {
            {"谷物类", "GRAIN", "水稻、小麦、玉米等粮食作物种子"},
            {"蔬菜类", "VEGETABLE", "番茄、黄瓜、白菜等蔬菜种子"},
            {"水果类", "FRUIT", "西瓜、草莓、苹果等水果种子"},
            {"油料类", "OIL", "大豆、花生、油菜等油料作物种子"},
            {"花卉类", "FLOWER", "玫瑰、菊花、百合等花卉种子"}
        };

        for (String[] cat : categories) {
            Category category = new Category();
            category.setName(cat[0]);
            category.setCode(cat[1]);
            category.setDescription(cat[2]);
            category.setStatus(1);
            categoryRepository.save(category);
        }
    }

    /**
     * 初始化品种数据
     */
    private void initVarieties() {
        System.out.println("初始化品种数据...");
        
        // 获取品类
        Category grain = categoryRepository.findByCode("GRAIN");
        Category vegetable = categoryRepository.findByCode("VEGETABLE");
        Category fruit = categoryRepository.findByCode("FRUIT");
        Category oil = categoryRepository.findByCode("OIL");
        Category flower = categoryRepository.findByCode("FLOWER");

        // 谷物类品种
        Variety v1 = new Variety();
        v1.setName("水稻一号");
        v1.setCode("RICE-001");
        v1.setCategoryId(grain.getId());
        v1.setDescription("高产优质水稻品种，抗倒伏");
        v1.setManufacturer("中种集团");
        v1.setShelfLifeDays(540);
        v1.setOptimalTempMin(5.0);
        v1.setOptimalTempMax(20.0);
        v1.setOptimalHumidityMin(40.0);
        v1.setOptimalHumidityMax(60.0);
        v1.setStatus(1);
        varietyRepository.save(v1);

        Variety v2 = new Variety();
        v2.setName("小麦二号");
        v2.setCode("WHEAT-002");
        v2.setCategoryId(grain.getId());
        v2.setDescription("强筋小麦，适合制作面包");
        v2.setManufacturer("中种集团");
        v2.setShelfLifeDays(540);
        v2.setStatus(1);
        varietyRepository.save(v2);

        // 蔬菜类品种
        Variety v3 = new Variety();
        v3.setName("番茄三号");
        v3.setCode("TOMATO-003");
        v3.setCategoryId(vegetable.getId());
        v3.setDescription("早熟番茄，抗病性强");
        v3.setManufacturer("寿光种业");
        v3.setShelfLifeDays(365);
        v3.setOptimalTempMin(8.0);
        v3.setOptimalTempMax(18.0);
        v3.setOptimalHumidityMin(35.0);
        v3.setOptimalHumidityMax(65.0);
        v3.setStatus(1);
        varietyRepository.save(v3);

        Variety v4 = new Variety();
        v4.setName("黄瓜四号");
        v4.setCode("CUCUMBER-004");
        v4.setCategoryId(vegetable.getId());
        v4.setDescription("高产黄瓜，口感脆嫩");
        v4.setManufacturer("寿光种业");
        v4.setShelfLifeDays(365);
        v4.setStatus(1);
        varietyRepository.save(v4);

        Variety v5 = new Variety();
        v5.setName("白菜五号");
        v5.setCode("CABBAGE-005");
        v5.setCategoryId(vegetable.getId());
        v5.setDescription("优质大白菜，耐储存");
        v5.setManufacturer("山东种业");
        v5.setShelfLifeDays(365);
        v5.setStatus(1);
        varietyRepository.save(v5);

        // 水果类品种
        Variety v6 = new Variety();
        v6.setName("西瓜六号");
        v6.setCode("WATERMELON-006");
        v6.setCategoryId(fruit.getId());
        v6.setDescription("8424西瓜，甜度高");
        v6.setManufacturer("新疆种业");
        v6.setShelfLifeDays(365);
        v6.setStatus(1);
        varietyRepository.save(v6);

        // 油料类品种
        Variety v7 = new Variety();
        v7.setName("大豆七号");
        v7.setCode("SOYBEAN-007");
        v7.setCategoryId(oil.getId());
        v7.setDescription("高蛋白大豆品种");
        v7.setManufacturer("中粮集团");
        v7.setShelfLifeDays(540);
        v7.setStatus(1);
        varietyRepository.save(v7);

        // 花卉类品种
        Variety v8 = new Variety();
        v8.setName("玫瑰八号");
        v8.setCode("ROSE-008");
        v8.setCategoryId(flower.getId());
        v8.setDescription("红玫瑰，花期长");
        v8.setManufacturer("云南花卉");
        v8.setShelfLifeDays(365);
        v8.setStatus(1);
        varietyRepository.save(v8);
    }

    /**
     * 初始化仓库数据
     */
    private void initWarehouses() {
        System.out.println("初始化仓库数据...");

        Warehouse w1 = new Warehouse();
        w1.setName("中心仓库");
        w1.setCode("WH-CENTER");
        w1.setAddress("北京市朝阳区工业园区88号");
        w1.setManager("张三");
        w1.setPhone("13800138001");
        w1.setArea(2000.0);
        w1.setType(1);
        w1.setCurrentTemperature(20.0);
        w1.setCurrentHumidity(50.0);
        w1.setTempWarningMin(0.0);
        w1.setTempWarningMax(30.0);
        w1.setHumidityWarningMin(30.0);
        w1.setHumidityWarningMax(70.0);
        w1.setStatus(1);
        warehouseRepository.save(w1);

        Warehouse w2 = new Warehouse();
        w2.setName("冷藏仓库");
        w2.setCode("WH-COLD");
        w2.setAddress("北京市海淀区科技路66号");
        w2.setManager("李四");
        w2.setPhone("13800138002");
        w2.setArea(1000.0);
        w2.setType(2);
        w2.setCurrentTemperature(5.0);
        w2.setCurrentHumidity(60.0);
        w2.setTempWarningMin(0.0);
        w2.setTempWarningMax(10.0);
        w2.setHumidityWarningMin(50.0);
        w2.setHumidityWarningMax(80.0);
        w2.setStatus(1);
        warehouseRepository.save(w2);

        Warehouse w3 = new Warehouse();
        w3.setName("恒温恒湿仓库");
        w3.setCode("WH-CONSTANT");
        w3.setAddress("北京市丰台区创新路100号");
        w3.setManager("王五");
        w3.setPhone("13800138003");
        w3.setArea(800.0);
        w3.setType(3);
        w3.setCurrentTemperature(15.0);
        w3.setCurrentHumidity(55.0);
        w3.setTempWarningMin(10.0);
        w3.setTempWarningMax(20.0);
        w3.setHumidityWarningMin(50.0);
        w3.setHumidityWarningMax(60.0);
        w3.setStatus(1);
        warehouseRepository.save(w3);
    }

    /**
     * 初始化门店数据
     */
    private void initStores() {
        System.out.println("初始化门店数据...");

        Store s1 = new Store();
        s1.setName("朝阳直营店");
        s1.setCode("STORE-001");
        s1.setAddress("北京市朝阳区建国路88号");
        s1.setManager("赵六");
        s1.setPhone("13900139001");
        s1.setType(1);
        s1.setRegion("华北区");
        s1.setStatus(1);
        storeRepository.save(s1);

        Store s2 = new Store();
        s2.setName("海淀加盟店");
        s2.setCode("STORE-002");
        s2.setAddress("北京市海淀区中关村大街1号");
        s2.setManager("钱七");
        s2.setPhone("13900139002");
        s2.setType(2);
        s2.setRegion("华北区");
        s2.setStatus(1);
        storeRepository.save(s2);

        Store s3 = new Store();
        s3.setName("丰台经销店");
        s3.setCode("STORE-003");
        s3.setAddress("北京市丰台区南三环西路58号");
        s3.setManager("孙八");
        s3.setPhone("13900139003");
        s3.setType(3);
        s3.setRegion("华北区");
        s3.setStatus(1);
        storeRepository.save(s3);
    }

    /**
     * 初始化种子批次数据
     */
    private void initSeedBatches() {
        System.out.println("初始化种子批次数据...");

        LocalDate today = LocalDate.now();

        // 获取品种
        Variety rice = varietyRepository.findByCode("RICE-001");
        Variety wheat = varietyRepository.findByCode("WHEAT-002");
        Variety tomato = varietyRepository.findByCode("TOMATO-003");
        Variety cucumber = varietyRepository.findByCode("CUCUMBER-004");
        Variety cabbage = varietyRepository.findByCode("CABBAGE-005");
        Variety watermelon = varietyRepository.findByCode("WATERMELON-006");
        Variety soybean = varietyRepository.findByCode("SOYBEAN-007");
        Variety rose = varietyRepository.findByCode("ROSE-008");

        // 创建批次 - 正常批次
        SeedBatch b1 = new SeedBatch();
        b1.setBatchNo("B" + today.getYear() + today.getMonthValue() + "001");
        b1.setVarietyId(rice.getId());
        b1.setProductionDate(today.minusDays(30));
        b1.setExpiryDate(today.plusDays(510));
        b1.setTotalQuantity(10000);
        b1.setAvailableQuantity(10000);
        b1.setUnit("公斤");
        b1.setUnitPrice(50.0);
        b1.setSupplier("中种集团");
        b1.setStatus(0);
        seedBatchRepository.save(b1);

        SeedBatch b2 = new SeedBatch();
        b2.setBatchNo("B" + today.getYear() + today.getMonthValue() + "002");
        b2.setVarietyId(wheat.getId());
        b2.setProductionDate(today.minusDays(60));
        b2.setExpiryDate(today.plusDays(480));
        b2.setTotalQuantity(8000);
        b2.setAvailableQuantity(8000);
        b2.setUnit("公斤");
        b2.setUnitPrice(45.0);
        b2.setSupplier("中种集团");
        b2.setStatus(0);
        seedBatchRepository.save(b2);

        SeedBatch b3 = new SeedBatch();
        b3.setBatchNo("B" + today.getYear() + today.getMonthValue() + "003");
        b3.setVarietyId(tomato.getId());
        b3.setProductionDate(today.minusDays(15));
        b3.setExpiryDate(today.plusDays(350));
        b3.setTotalQuantity(5000);
        b3.setAvailableQuantity(5000);
        b3.setUnit("袋");
        b3.setUnitPrice(25.0);
        b3.setSupplier("寿光种业");
        b3.setStatus(0);
        seedBatchRepository.save(b3);

        SeedBatch b4 = new SeedBatch();
        b4.setBatchNo("B" + today.getYear() + today.getMonthValue() + "004");
        b4.setVarietyId(cucumber.getId());
        b4.setProductionDate(today.minusDays(20));
        b4.setExpiryDate(today.plusDays(345));
        b4.setTotalQuantity(6000);
        b4.setAvailableQuantity(6000);
        b4.setUnit("袋");
        b4.setUnitPrice(20.0);
        b4.setSupplier("寿光种业");
        b4.setStatus(0);
        seedBatchRepository.save(b4);

        SeedBatch b5 = new SeedBatch();
        b5.setBatchNo("B" + today.getYear() + today.getMonthValue() + "005");
        b5.setVarietyId(cabbage.getId());
        b5.setProductionDate(today.minusDays(10));
        b5.setExpiryDate(today.plusDays(355));
        b5.setTotalQuantity(4000);
        b5.setAvailableQuantity(4000);
        b5.setUnit("袋");
        b5.setUnitPrice(15.0);
        b5.setSupplier("山东种业");
        b5.setStatus(0);
        seedBatchRepository.save(b5);

        // 近效期批次（20天后过期）
        SeedBatch b6 = new SeedBatch();
        b6.setBatchNo("B" + today.getYear() + today.getMonthValue() + "006");
        b6.setVarietyId(watermelon.getId());
        b6.setProductionDate(today.minusDays(345));
        b6.setExpiryDate(today.plusDays(20));
        b6.setTotalQuantity(2000);
        b6.setAvailableQuantity(2000);
        b6.setUnit("袋");
        b6.setUnitPrice(30.0);
        b6.setSupplier("新疆种业");
        b6.setStatus(1);
        seedBatchRepository.save(b6);

        // 已过期批次
        SeedBatch b7 = new SeedBatch();
        b7.setBatchNo("B" + today.getYear() + today.getMonthValue() + "007");
        b7.setVarietyId(rose.getId());
        b7.setProductionDate(today.minusDays(400));
        b7.setExpiryDate(today.minusDays(35));
        b7.setTotalQuantity(500);
        b7.setAvailableQuantity(500);
        b7.setUnit("包");
        b7.setUnitPrice(100.0);
        b7.setSupplier("云南花卉");
        b7.setStatus(2);
        seedBatchRepository.save(b7);

        SeedBatch b8 = new SeedBatch();
        b8.setBatchNo("B" + today.getYear() + today.getMonthValue() + "008");
        b8.setVarietyId(soybean.getId());
        b8.setProductionDate(today.minusDays(45));
        b8.setExpiryDate(today.plusDays(495));
        b8.setTotalQuantity(7000);
        b8.setAvailableQuantity(7000);
        b8.setUnit("公斤");
        b8.setUnitPrice(55.0);
        b8.setSupplier("中粮集团");
        b8.setStatus(0);
        seedBatchRepository.save(b8);
    }

    /**
     * 初始化库存数据
     */
    private void initInventory() {
        System.out.println("初始化库存数据...");

        LocalDate today = LocalDate.now();

        // 获取仓库和门店
        Warehouse centerWh = warehouseRepository.findByCode("WH-CENTER");
        Warehouse coldWh = warehouseRepository.findByCode("WH-COLD");
        Store store1 = storeRepository.findByCode("STORE-001");
        Store store2 = storeRepository.findByCode("STORE-002");

        // 获取批次
        SeedBatch batch1 = seedBatchRepository.findByBatchNo("B" + today.getYear() + today.getMonthValue() + "001");
        SeedBatch batch2 = seedBatchRepository.findByBatchNo("B" + today.getYear() + today.getMonthValue() + "002");
        SeedBatch batch3 = seedBatchRepository.findByBatchNo("B" + today.getYear() + today.getMonthValue() + "003");
        SeedBatch batch4 = seedBatchRepository.findByBatchNo("B" + today.getYear() + today.getMonthValue() + "004");
        SeedBatch batch5 = seedBatchRepository.findByBatchNo("B" + today.getYear() + today.getMonthValue() + "005");
        SeedBatch batch6 = seedBatchRepository.findByBatchNo("B" + today.getYear() + today.getMonthValue() + "006");
        SeedBatch batch8 = seedBatchRepository.findByBatchNo("B" + today.getYear() + today.getMonthValue() + "008");

        // 中心仓库库存
        addInventory(1, centerWh.getId(), batch1.getId(), 5000, today.minusDays(25), 1000, 10000);
        addInventory(1, centerWh.getId(), batch2.getId(), 4000, today.minusDays(55), 500, 8000);
        addInventory(1, centerWh.getId(), batch8.getId(), 3500, today.minusDays(40), 500, 8000);

        // 冷藏仓库库存（存放需要低温保存的蔬菜种子）
        addInventory(1, coldWh.getId(), batch3.getId(), 3000, today.minusDays(10), 100, 5000);
        addInventory(1, coldWh.getId(), batch4.getId(), 4000, today.minusDays(15), 100, 6000);
        addInventory(1, coldWh.getId(), batch5.getId(), 2000, today.minusDays(5), 100, 4000);
        addInventory(1, coldWh.getId(), batch6.getId(), 2000, today.minusDays(300), 100, 3000);

        // 门店库存
        addInventory(2, store1.getId(), batch3.getId(), 500, today.minusDays(5), 50, 1000);
        addInventory(2, store1.getId(), batch4.getId(), 600, today.minusDays(5), 50, 1000);
        addInventory(2, store2.getId(), batch5.getId(), 800, today.minusDays(3), 50, 1500);
        addInventory(2, store2.getId(), batch1.getId(), 200, today.minusDays(3), 50, 500);
    }

    private void addInventory(Integer type, Long locationId, Long batchId, 
                              Integer qty, LocalDate inboundDate, Integer minStock, Integer maxStock) {
        Inventory inv = new Inventory();
        inv.setInventoryType(type);
        inv.setLocationId(locationId);
        inv.setBatchId(batchId);
        inv.setQuantity(qty);
        inv.setLockedQuantity(0);
        inv.setAvailableQuantity(qty);
        inv.setInboundDate(inboundDate);
        inv.setStatus(0);
        inv.setMinStockWarning(minStock);
        inv.setMaxStockWarning(maxStock);
        inventoryRepository.save(inv);
    }

    /**
     * 初始化环境记录数据
     */
    private void initEnvRecords() {
        System.out.println("初始化环境记录数据...");

        LocalDateTime now = LocalDateTime.now();

        Warehouse centerWh = warehouseRepository.findByCode("WH-CENTER");
        Warehouse coldWh = warehouseRepository.findByCode("WH-COLD");
        Warehouse constantWh = warehouseRepository.findByCode("WH-CONSTANT");

        // 生成最近24小时的环境记录（每2小时一条）
        for (int i = 0; i < 12; i++) {
            LocalDateTime time = now.minusHours(i * 2);
            
            // 中心仓库 - 正常
            addEnvRecord(centerWh.getId(), time, 18.0 + Math.random() * 4, 45.0 + Math.random() * 10);
            
            // 冷藏仓库 - 正常
            addEnvRecord(coldWh.getId(), time, 4.0 + Math.random() * 2, 55.0 + Math.random() * 10);
            
            // 恒温恒湿仓库 - 故意制造一些异常数据用于测试
            double temp = i % 4 == 0 ? 25.0 : 15.0; // 每隔8小时温度异常
            double humidity = i % 6 == 0 ? 85.0 : 55.0; // 每隔12小时湿度异常
            addEnvRecord(constantWh.getId(), time, temp, humidity);
        }
    }

    private void addEnvRecord(Long warehouseId, LocalDateTime time, double temp, double humidity) {
        EnvRecord record = new EnvRecord();
        record.setWarehouseId(warehouseId);
        record.setRecordTime(time);
        record.setTemperature(temp);
        record.setHumidity(humidity);
        record.setSourceType(1);

        // 获取仓库阈值
        Warehouse wh = warehouseRepository.findById(warehouseId).orElse(null);
        if (wh != null) {
            double tempMin = wh.getTempWarningMin() != null ? wh.getTempWarningMin() : 0.0;
            double tempMax = wh.getTempWarningMax() != null ? wh.getTempWarningMax() : 35.0;
            double humMin = wh.getHumidityWarningMin() != null ? wh.getHumidityWarningMin() : 30.0;
            double humMax = wh.getHumidityWarningMax() != null ? wh.getHumidityWarningMax() : 70.0;

            record.setTempThresholdMin(tempMin);
            record.setTempThresholdMax(tempMax);
            record.setHumidityThresholdMin(humMin);
            record.setHumidityThresholdMax(humMax);

            boolean tempAbnormal = temp < tempMin || temp > tempMax;
            boolean humAbnormal = humidity < humMin || humidity > humMax;

            if (tempAbnormal && humAbnormal) {
                record.setStatus(3);
                record.setWarningMessage("温度和湿度均超出正常范围！");
            } else if (tempAbnormal) {
                record.setStatus(1);
                record.setWarningMessage("温度超出正常范围！");
            } else if (humAbnormal) {
                record.setStatus(2);
                record.setWarningMessage("湿度超出正常范围！");
            } else {
                record.setStatus(0);
            }
        }

        envRecordRepository.save(record);
    }
}
