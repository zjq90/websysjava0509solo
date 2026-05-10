package com.appsys.common.config;

import com.appsys.common.enums.RoleEnum;
import com.appsys.common.util.AESUtil;
import com.appsys.field.entity.FieldRecord;
import com.appsys.field.repository.FieldRecordRepository;
import com.appsys.inventory.entity.Inventory;
import com.appsys.inventory.entity.Seed;
import com.appsys.inventory.repository.InventoryRepository;
import com.appsys.inventory.repository.SeedRepository;
import com.appsys.order.entity.Customer;
import com.appsys.order.entity.Order;
import com.appsys.order.entity.OrderItem;
import com.appsys.order.repository.CustomerRepository;
import com.appsys.order.repository.OrderRepository;
import com.appsys.system.entity.SysPermission;
import com.appsys.system.entity.SysRole;
import com.appsys.system.entity.SysUser;
import com.appsys.system.repository.SysPermissionRepository;
import com.appsys.system.repository.SysRoleRepository;
import com.appsys.system.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据初始化器
 * 应用启动时初始化用户、角色、权限数据
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SysUserRepository userRepository;

    @Autowired
    private SysRoleRepository roleRepository;

    @Autowired
    private SysPermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AESUtil aesUtil;

    @Autowired
    private SeedRepository seedRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private FieldRecordRepository fieldRecordRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            initPermissions();
            initRoles();
            initUsers();
            initSeeds();
            initInventory();
            initCustomers();
            initOrders();
            initFieldRecords();
        }
    }

    /**
     * 初始化权限数据
     */
    private void initPermissions() {
        // 库存管理权限
        createPermission("inventory:view", "库存查看", "menu", "/inventory", null, null, "inventory", "查看库存");
        createPermission("inventory:create", "添加库存", "button", null, "/api/inventory", "POST", null, "添加库存记录");
        createPermission("inventory:update", "修改库存", "button", null, "/api/inventory/**", "PUT", null, "修改库存记录");
        createPermission("inventory:delete", "删除库存", "button", null, "/api/inventory/**", "DELETE", null, "删除库存记录");

        // 订单管理权限
        createPermission("order:view", "订单查看", "menu", "/order", null, null, "order", "查看订单");
        createPermission("order:create", "创建订单", "button", null, "/api/order", "POST", null, "创建订单");
        createPermission("order:update", "修改订单", "button", null, "/api/order/**", "PUT", null, "修改订单");
        createPermission("order:delete", "删除订单", "button", null, "/api/order/**", "DELETE", null, "删除订单");

        // 田间记录权限
        createPermission("field:view", "田间记录查看", "menu", "/field", null, null, "field", "查看田间记录");
        createPermission("field:create", "添加田间记录", "button", null, "/api/field", "POST", null, "添加田间记录");
        createPermission("field:update", "修改田间记录", "button", null, "/api/field/**", "PUT", null, "修改田间记录");
        createPermission("field:delete", "删除田间记录", "button", null, "/api/field/**", "DELETE", null, "删除田间记录");

        // 报表权限
        createPermission("report:view", "报表查看", "menu", "/report", null, null, "report", "查看报表");
    }

    /**
     * 创建权限
     */
    private void createPermission(String code, String name, String type, String path, String url, String method, String icon, String desc) {
        SysPermission permission = new SysPermission();
        permission.setPermissionCode(code);
        permission.setPermissionName(name);
        permission.setPermissionType(type);
        permission.setPath(path);
        permission.setUrl(url);
        permission.setMethod(method);
        permission.setIcon(icon);
        permission.setDescription(desc);
        permission.setCreatedBy("system");
        permission.setCreatedTime(LocalDateTime.now());
        permissionRepository.save(permission);
    }

    /**
     * 初始化角色数据
     */
    private void initRoles() {
        // 仓管员角色
        createRole(RoleEnum.WAREHOUSE_KEEPER.getCode(), RoleEnum.WAREHOUSE_KEEPER.getName(), 
                RoleEnum.WAREHOUSE_KEEPER.getDescription(),
                "inventory:view", "inventory:create", "inventory:update", "inventory:delete", "report:view");

        // 销售员角色
        createRole(RoleEnum.SALESMAN.getCode(), RoleEnum.SALESMAN.getName(), 
                RoleEnum.SALESMAN.getDescription(),
                "order:view", "order:create", "order:update", "order:delete", "report:view");

        // 农技员角色
        createRole(RoleEnum.AGRICULTURAL_TECHNICIAN.getCode(), RoleEnum.AGRICULTURAL_TECHNICIAN.getName(), 
                RoleEnum.AGRICULTURAL_TECHNICIAN.getDescription(),
                "field:view", "field:create", "field:update", "field:delete", "report:view");

        // 管理层角色
        createRole(RoleEnum.MANAGER.getCode(), RoleEnum.MANAGER.getName(), 
                RoleEnum.MANAGER.getDescription(),
                "inventory:view", "inventory:create", "inventory:update", "inventory:delete",
                "order:view", "order:create", "order:update", "order:delete",
                "field:view", "field:create", "field:update", "field:delete",
                "report:view");

        // 管理员角色
        createRole(RoleEnum.ADMIN.getCode(), RoleEnum.ADMIN.getName(), 
                RoleEnum.ADMIN.getDescription(),
                "inventory:view", "inventory:create", "inventory:update", "inventory:delete",
                "order:view", "order:create", "order:update", "order:delete",
                "field:view", "field:create", "field:update", "field:delete",
                "report:view");
    }

    /**
     * 创建角色
     */
    private void createRole(String code, String name, String desc, String... permissionCodes) {
        SysRole role = new SysRole();
        role.setRoleCode(code);
        role.setRoleName(name);
        role.setDescription(desc);
        role.setCreatedBy("system");
        role.setCreatedTime(LocalDateTime.now());

        Set<SysPermission> permissions = new HashSet<>();
        for (String permCode : permissionCodes) {
            permissionRepository.findByPermissionCode(permCode).ifPresent(permissions::add);
        }
        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    /**
     * 初始化用户数据
     */
    private void initUsers() {
        // 仓管员
        createUser("warehouse", "123456", "张三", "13800138001", "warehouse@example.com", RoleEnum.WAREHOUSE_KEEPER.getCode());

        // 销售员
        createUser("salesman", "123456", "李四", "13800138002", "salesman@example.com", RoleEnum.SALESMAN.getCode());

        // 农技员
        createUser("agricultural", "123456", "王五", "13800138003", "agricultural@example.com", RoleEnum.AGRICULTURAL_TECHNICIAN.getCode());

        // 管理层
        createUser("manager", "123456", "赵六", "13800138004", "manager@example.com", RoleEnum.MANAGER.getCode());

        // 系统管理员
        createUser("admin", "123456", "系统管理员", "13900139001", "admin@example.com", RoleEnum.ADMIN.getCode());
    }

    /**
     * 创建用户
     */
    private void createUser(String username, String password, String realName, String phone, String email, String roleCode) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRealName(realName);
        user.setPhone(aesUtil.encrypt(phone));
        user.setEmail(email);
        user.setStatus(1);
        user.setCreatedBy("system");
        user.setCreatedTime(LocalDateTime.now());

        Set<SysRole> roles = new HashSet<>();
        roleRepository.findByRoleCode(roleCode).ifPresent(roles::add);
        user.setRoles(roles);
        userRepository.save(user);
    }

    /**
     * 初始化种子数据
     */
    private void initSeeds() {
        createSeed("水稻种子-超级稻", "水稻", "500g/袋", "袋", new BigDecimal("15.00"), new BigDecimal("25.00"), new BigDecimal("95.5"), LocalDate.now().plusMonths(24), "种子供应商A");
        createSeed("小麦种子-新麦26", "小麦", "1kg/袋", "袋", new BigDecimal("12.00"), new BigDecimal("20.00"), new BigDecimal("92.0"), LocalDate.now().plusMonths(18), "种子供应商B");
        createSeed("玉米种子-登海605", "玉米", "2kg/袋", "袋", new BigDecimal("35.00"), new BigDecimal("55.00"), new BigDecimal("90.0"), LocalDate.now().plusMonths(12), "种子供应商C");
        createSeed("大豆种子-中黄13", "大豆", "1kg/袋", "袋", new BigDecimal("20.00"), new BigDecimal("32.00"), new BigDecimal("88.5"), LocalDate.now().plusMonths(15), "种子供应商A");
        createSeed("蔬菜种子-番茄", "蔬菜", "10g/袋", "袋", new BigDecimal("8.00"), new BigDecimal("15.00"), new BigDecimal("93.0"), LocalDate.now().plusMonths(9), "种子供应商D");
    }

    /**
     * 创建种子
     */
    private Seed createSeed(String seedName, String category, String specification, String unit, 
                            BigDecimal purchasePrice, BigDecimal salePrice, BigDecimal germinationRate,
                            LocalDate expiryDate, String supplier) {
        Seed seed = new Seed();
        seed.setSeedName(seedName);
        seed.setCategory(category);
        seed.setSpecification(specification);
        seed.setUnit(unit);
        seed.setPurchasePrice(purchasePrice);
        seed.setSalePrice(salePrice);
        seed.setGerminationRate(germinationRate);
        seed.setExpiryDate(expiryDate);
        seed.setSupplier(supplier);
        seed.setCreatedBy("system");
        seed.setCreatedTime(LocalDateTime.now());
        return seedRepository.save(seed);
    }

    /**
     * 初始化库存数据
     */
    private void initInventory() {
        List<Seed> seeds = seedRepository.findAll();
        if (seeds.size() >= 5) {
            createInventory("BAT00001", seeds.get(0), new BigDecimal("1000.00"), LocalDate.now().minusDays(30), LocalDate.now().plusMonths(24), new BigDecimal("95.5"), new BigDecimal("15.00"), "A区01号库");
            createInventory("BAT00002", seeds.get(1), new BigDecimal("800.00"), LocalDate.now().minusDays(20), LocalDate.now().plusMonths(18), new BigDecimal("92.0"), new BigDecimal("12.00"), "A区02号库");
            createInventory("BAT00003", seeds.get(2), new BigDecimal("500.00"), LocalDate.now().minusDays(15), LocalDate.now().plusMonths(12), new BigDecimal("90.0"), new BigDecimal("35.00"), "B区01号库");
            createInventory("BAT00004", seeds.get(3), new BigDecimal("600.00"), LocalDate.now().minusDays(10), LocalDate.now().plusMonths(15), new BigDecimal("88.5"), new BigDecimal("20.00"), "B区02号库");
            createInventory("BAT00005", seeds.get(4), new BigDecimal("300.00"), LocalDate.now().minusDays(5), LocalDate.now().plusMonths(9), new BigDecimal("93.0"), new BigDecimal("8.00"), "C区01号库");
        }
    }

    /**
     * 创建库存记录
     */
    private Inventory createInventory(String batchNo, Seed seed, BigDecimal quantity, LocalDate inDate,
                                      LocalDate expiryDate, BigDecimal germinationRate, BigDecimal unitPrice,
                                      String warehouseLocation) {
        Inventory inventory = new Inventory();
        inventory.setBatchNo(batchNo);
        inventory.setSeedId(seed.getId());
        inventory.setSeedName(seed.getSeedName());
        inventory.setQuantity(quantity);
        inventory.setRemainingQuantity(quantity);
        inventory.setInDate(inDate);
        inventory.setExpiryDate(expiryDate);
        inventory.setGerminationRate(germinationRate);
        inventory.setUnitPrice(unitPrice);
        inventory.setTotalPrice(unitPrice.multiply(quantity));
        inventory.setWarehouseLocation(warehouseLocation);
        inventory.setStatus(1);
        inventory.setCreatedBy("system");
        inventory.setCreatedTime(LocalDateTime.now());
        return inventoryRepository.save(inventory);
    }

    /**
     * 初始化客户数据
     */
    private void initCustomers() {
        createCustomer("王家庄种植合作社", "13812345678", "河北省石家庄市王家庄村", "wang@example.com", "合作社");
        createCustomer("李家农场", "13987654321", "山东省济南市李家村", "li@example.com", "农场");
        createCustomer("张大婶农资店", "13665432198", "河南省郑州市中原区", "zhang@example.com", "经销商");
        createCustomer("绿源生态农业", "13798765432", "江苏省南京市江宁区", "lvyuan@example.com", "企业");
        createCustomer("丰收种植大户", "13556789012", "安徽省合肥市肥东县", "fengshou@example.com", "种植大户");
    }

    /**
     * 创建客户
     */
    private Customer createCustomer(String customerName, String phone, String address, String email, String customerType) {
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setPhone(aesUtil.encrypt(phone));
        customer.setAddress(address);
        customer.setEmail(email);
        customer.setCustomerType(customerType);
        customer.setCreatedBy("system");
        customer.setCreatedTime(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    /**
     * 初始化订单数据
     */
    private void initOrders() {
        List<Customer> customers = customerRepository.findAll();
        List<Inventory> inventories = inventoryRepository.findAll();
        
        if (!customers.isEmpty() && !inventories.isEmpty()) {
            // 创建订单1 - 待处理
            Order order1 = createOrder("ORD202501001", customers.get(0), new BigDecimal("2500.00"), 1, "李四", "常规订单");
            createOrderItem(order1, inventories.get(0), new BigDecimal("100.00"), new BigDecimal("25.00"));
            orderRepository.save(order1);
            
            // 创建订单2 - 处理中
            Order order2 = createOrder("ORD202501002", customers.get(1), new BigDecimal("1600.00"), 2, "李四", "加急订单");
            createOrderItem(order2, inventories.get(1), new BigDecimal("80.00"), new BigDecimal("20.00"));
            orderRepository.save(order2);
            
            // 创建订单3 - 已完成
            Order order3 = createOrder("ORD202501003", customers.get(2), new BigDecimal("5500.00"), 3, "李四", "大客户订单");
            createOrderItem(order3, inventories.get(2), new BigDecimal("100.00"), new BigDecimal("55.00"));
            orderRepository.save(order3);
        }
    }

    /**
     * 创建订单
     */
    private Order createOrder(String orderNo, Customer customer, BigDecimal amount, Integer status,
                              String salesmanName, String remark) {
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setCustomerId(customer.getId());
        order.setCustomerName(customer.getCustomerName());
        order.setOrderDate(LocalDateTime.now());
        order.setOrderAmount(amount);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setActualAmount(amount);
        order.setStatus(status);
        order.setSalesmanName(salesmanName);
        order.setRemark(remark);
        order.setCreatedBy("system");
        order.setCreatedTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * 创建订单明细
     */
    private void createOrderItem(Order order, Inventory inventory, BigDecimal quantity, BigDecimal price) {
        OrderItem item = new OrderItem();
        item.setOrderId(order.getId());
        item.setInventoryId(inventory.getId());
        item.setSeedName(inventory.getSeedName());
        item.setBatchNo(inventory.getBatchNo());
        item.setQuantity(quantity);
        item.setUnitPrice(price);
        item.setAmount(quantity.multiply(price));
        item.setCreatedBy("system");
        item.setCreatedTime(LocalDateTime.now());
        
        if (order.getItems() == null) {
            order.setItems(new ArrayList<>());
        }
        order.getItems().add(item);
    }

    /**
     * 初始化田间记录数据
     */
    private void initFieldRecords() {
        createFieldRecord("试验田A区", "水稻", LocalDate.now().minusDays(5), new BigDecimal("26.5"), new BigDecimal("65.0"), "无病虫害", "已施用复合肥50kg/亩", "滴灌浇水3小时", "水稻生长状况良好");
        createFieldRecord("试验田B区", "小麦", LocalDate.now().minusDays(3), new BigDecimal("22.0"), new BigDecimal("72.0"), "轻微蚜虫", "已喷施叶面肥", "自然降雨", "注意防治蚜虫");
        createFieldRecord("示范田1号", "玉米", LocalDate.now().minusDays(7), new BigDecimal("28.0"), new BigDecimal("60.0"), "无病虫害", "已施基肥", "漫灌浇水", "玉米出苗整齐");
        createFieldRecord("示范田2号", "大豆", LocalDate.now().minusDays(2), new BigDecimal("25.5"), new BigDecimal("68.0"), "无病虫害", "无需施肥", "滴灌浇水2小时", "大豆进入开花期");
        createFieldRecord("大棚蔬菜区", "番茄", LocalDate.now().minusDays(1), new BigDecimal("30.0"), new BigDecimal("75.0"), "轻微灰霉病", "已喷施杀菌剂", "滴灌浇水", "注意通风降湿");
    }

    /**
     * 创建田间记录
     */
    private FieldRecord createFieldRecord(String fieldName, String cropName, LocalDate recordDate,
                                          BigDecimal temperature, BigDecimal humidity,
                                          String pestStatus, String fertilization, String irrigation,
                                          String remark) {
        FieldRecord record = new FieldRecord();
        record.setFieldName(fieldName);
        record.setCropName(cropName);
        record.setRecordDate(recordDate);
        record.setTemperature(temperature);
        record.setHumidity(humidity);
        record.setPestStatus(pestStatus);
        record.setFertilization(fertilization);
        record.setIrrigation(irrigation);
        record.setRemark(remark);
        record.setCreatedBy("system");
        record.setCreatedTime(LocalDateTime.now());
        return fieldRecordRepository.save(record);
    }
}
