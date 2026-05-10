package com.websys.config;

import com.websys.entity.*;
import com.websys.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试数据初始化类
 * 系统启动时自动初始化测试数据
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceStatusRepository deviceStatusRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Override
    public void run(String... args) {
        initPermissions();
        initMenus();
        initRoles();
        initUsers();
        initProducts();
        initDevices();
        initSlots();
        System.out.println("========================================");
        System.out.println("  测试数据初始化完成！");
        System.out.println("========================================");
    }

    private void initPermissions() {
        if (permissionRepository.count() > 0) return;

        List<Permission> permissions = Arrays.asList(
                createPermission("用户管理", "user:manage", "用户增删改查权限"),
                createPermission("角色管理", "role:manage", "角色增删改查权限"),
                createPermission("菜单管理", "menu:manage", "菜单增删改查权限"),
                createPermission("设备管理", "device:manage", "设备增删改查权限"),
                createPermission("远程控制", "remote:control", "远程控制设备权限"),
                createPermission("商品管理", "product:manage", "商品增删改查权限"),
                createPermission("货道管理", "slot:manage", "货道增删改查权限"),
                createPermission("财务报表", "finance:view", "财务报表查看权限")
        );
        permissionRepository.saveAll(permissions);
    }

    private void initMenus() {
        if (menuRepository.count() > 0) return;

        List<Menu> menus = new ArrayList<>();
        
        menus.add(createMenu("首页", null, "/dashboard", "Dashboard", "s-home", 0, 2, 1));
        
        Menu systemMenu = createMenu("系统管理", null, "/system", null, "setting", 1, 1, 1);
        menus.add(systemMenu);
        
        menus.add(createMenu("用户管理", systemMenu.getId(), "/system/user", "system/UserList", "user", 2, 2, 1));
        menus.add(createMenu("角色管理", systemMenu.getId(), "/system/role", "system/RoleList", "team", 3, 2, 1));
        menus.add(createMenu("菜单管理", systemMenu.getId(), "/system/menu", "system/MenuList", "menu", 4, 2, 1));
        
        Menu deviceMenu = createMenu("设备管理", null, "/device", null, "desktop", 2, 1, 1);
        menus.add(deviceMenu);
        
        menus.add(createMenu("设备列表", deviceMenu.getId(), "/device/list", "device/DeviceList", "appstore", 5, 2, 1));
        menus.add(createMenu("远程控制", deviceMenu.getId(), "/device/remote", "device/RemoteControl", "control", 6, 2, 1));
        
        Menu productMenu = createMenu("商品管理", null, "/product", null, "shopping", 3, 1, 1);
        menus.add(productMenu);
        
        menus.add(createMenu("商品列表", productMenu.getId(), "/product/list", "product/ProductList", "shop", 7, 2, 1));
        menus.add(createMenu("货道管理", productMenu.getId(), "/product/slot", "product/SlotList", "container", 8, 2, 1));
        
        Menu financeMenu = createMenu("财务管理", null, "/finance", null, "money-collect", 4, 1, 1);
        menus.add(financeMenu);
        
        menus.add(createMenu("财务报表", financeMenu.getId(), "/finance/report", "finance/FinanceReport", "bar-chart", 9, 2, 1));
        
        for (int i = 0; i < menus.size(); i++) {
            Menu saved = menuRepository.save(menus.get(i));
            menus.set(i, saved);
        }
        
        for (int i = 0; i < menus.size(); i++) {
            Menu menu = menus.get(i);
            if (menu.getPath().equals("/system/user")) {
                menu.setParentId(menus.get(1).getId());
            } else if (menu.getPath().equals("/system/role")) {
                menu.setParentId(menus.get(1).getId());
            } else if (menu.getPath().equals("/system/menu")) {
                menu.setParentId(menus.get(1).getId());
            } else if (menu.getPath().equals("/device/list")) {
                menu.setParentId(menus.get(5).getId());
            } else if (menu.getPath().equals("/device/remote")) {
                menu.setParentId(menus.get(5).getId());
            } else if (menu.getPath().equals("/product/list")) {
                menu.setParentId(menus.get(8).getId());
            } else if (menu.getPath().equals("/product/slot")) {
                menu.setParentId(menus.get(8).getId());
            } else if (menu.getPath().equals("/finance/report")) {
                menu.setParentId(menus.get(11).getId());
            }
            if (menu.getParentId() != null) {
                menuRepository.save(menu);
            }
        }
    }

    private void initRoles() {
        if (roleRepository.count() > 0) return;

        List<Permission> allPermissions = permissionRepository.findAll();
        List<Menu> allMenus = menuRepository.findAll();

        List<Menu> deviceMenus = getMenusByPaths(allMenus, Arrays.asList(
                "/dashboard", "/device", "/device/list", "/device/remote"
        ));

        List<Menu> operatorMenus = getMenusByPaths(allMenus, Arrays.asList(
                "/dashboard", "/device", "/device/list", 
                "/product", "/product/list", "/product/slot"
        ));

        List<Menu> maintainerMenus = getMenusByPaths(allMenus, Arrays.asList(
                "/dashboard", "/device", "/device/list", "/device/remote",
                "/product", "/product/list", "/product/slot"
        ));

        List<Menu> financeMenus = getMenusByPaths(allMenus, Arrays.asList(
                "/dashboard", "/finance", "/finance/report"
        ));

        Role adminRole = createRole("系统管理员", "ADMIN", "拥有系统所有权限");
        adminRole.setPermissions(allPermissions);
        adminRole.setMenus(allMenus);
        
        Role operatorRole = createRole("运营人员", "OPERATOR", "负责设备运营和商品管理");
        operatorRole.setPermissions(Arrays.asList(
                getPermissionByCode(allPermissions, "device:manage"),
                getPermissionByCode(allPermissions, "product:manage"),
                getPermissionByCode(allPermissions, "slot:manage")
        ));
        operatorRole.setMenus(operatorMenus);
        
        Role maintainerRole = createRole("维护人员", "MAINTAINER", "负责设备维护和远程控制");
        maintainerRole.setPermissions(Arrays.asList(
                getPermissionByCode(allPermissions, "device:manage"),
                getPermissionByCode(allPermissions, "remote:control"),
                getPermissionByCode(allPermissions, "slot:manage")
        ));
        maintainerRole.setMenus(maintainerMenus);
        
        Role financeRole = createRole("财务人员", "FINANCE", "负责财务相关管理");
        financeRole.setPermissions(Arrays.asList(
                getPermissionByCode(allPermissions, "finance:view")
        ));
        financeRole.setMenus(financeMenus);
        
        roleRepository.saveAll(Arrays.asList(adminRole, operatorRole, maintainerRole, financeRole));
    }

    private void initUsers() {
        if (userRepository.count() > 0) return;

        Role adminRole = roleRepository.findByCode("ADMIN").orElse(null);
        Role operatorRole = roleRepository.findByCode("OPERATOR").orElse(null);
        Role maintainerRole = roleRepository.findByCode("MAINTAINER").orElse(null);
        Role financeRole = roleRepository.findByCode("FINANCE").orElse(null);

        List<User> users = Arrays.asList(
                createUser("admin", "123456", "系统管理员", "13800138000", "admin@websys.com", adminRole),
                createUser("operator", "123456", "张三(运营)", "13800138001", "operator@websys.com", operatorRole),
                createUser("maintainer", "123456", "李四(维护)", "13800138002", "maintainer@websys.com", maintainerRole),
                createUser("finance", "123456", "王五(财务)", "13800138003", "finance@websys.com", financeRole)
        );
        userRepository.saveAll(users);
    }

    private void initProducts() {
        if (productRepository.count() > 0) return;

        List<Product> products = Arrays.asList(
                createProduct("P001", "可口可乐", "饮料", new BigDecimal("3.50"), new BigDecimal("2.00")),
                createProduct("P002", "百事可乐", "饮料", new BigDecimal("3.50"), new BigDecimal("2.00")),
                createProduct("P003", "农夫山泉", "饮料", new BigDecimal("2.00"), new BigDecimal("1.00")),
                createProduct("P004", "康师傅红烧牛肉面", "方便面", new BigDecimal("5.50"), new BigDecimal("3.00")),
                createProduct("P005", "统一老坛酸菜面", "方便面", new BigDecimal("5.50"), new BigDecimal("3.00")),
                createProduct("P006", "奥利奥饼干", "零食", new BigDecimal("8.00"), new BigDecimal("5.00")),
                createProduct("P007", "乐事薯片", "零食", new BigDecimal("7.50"), new BigDecimal("4.50")),
                createProduct("P008", "雀巢咖啡", "饮料", new BigDecimal("6.00"), new BigDecimal("3.50"))
        );
        productRepository.saveAll(products);
    }

    private void initDevices() {
        if (deviceRepository.count() > 0) return;

        List<Device> devices = Arrays.asList(
                createDevice("DEV001", "A栋1楼自动售卖机", "VM-2024", "v1.2.3", 
                        "北京市朝阳区科技园区A栋1楼大厅", new BigDecimal("39.9042"), new BigDecimal("116.4074"), 
                        "张三", "13800138001", 1),
                createDevice("DEV002", "A栋2楼自动售卖机", "VM-2024", "v1.2.3", 
                        "北京市朝阳区科技园区A栋2楼电梯口", new BigDecimal("39.9042"), new BigDecimal("116.4075"), 
                        "张三", "13800138001", 1),
                createDevice("DEV003", "B栋1楼自动售卖机", "VM-2024", "v1.2.2", 
                        "北京市朝阳区科技园区B栋1楼大厅", new BigDecimal("39.9043"), new BigDecimal("116.4073"), 
                        "李四", "13800138002", 2),
                createDevice("DEV004", "C栋1楼自动售卖机", "VM-2024", "v1.2.3", 
                        "北京市朝阳区科技园区C栋1楼餐厅门口", new BigDecimal("39.9041"), new BigDecimal("116.4076"), 
                        "王五", "13800138003", 3),
                createDevice("DEV005", "D栋自动售卖机", "VM-2024-Pro", "v1.3.0", 
                        "北京市朝阳区科技园区D栋宿舍楼楼下", new BigDecimal("39.9040"), new BigDecimal("116.4072"), 
                        "赵六", "13800138004", 1)
        );
        deviceRepository.saveAll(devices);

        for (Device device : devices) {
            DeviceStatus status = createDeviceStatus(device);
            deviceStatusRepository.save(status);
        }
    }

    private void initSlots() {
        if (slotRepository.count() > 0) return;

        List<Device> devices = deviceRepository.findAll();
        List<Product> products = productRepository.findAll();

        for (Device device : devices) {
            for (int i = 1; i <= 10; i++) {
                Product product = products.get((i - 1) % products.size());
                Slot slot = createSlot(device, i, product);
                slotRepository.save(slot);
            }
        }
    }

    private Permission createPermission(String name, String code, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setCode(code);
        permission.setDescription(description);
        permission.setStatus(1);
        permission.setCreateTime(LocalDateTime.now());
        return permission;
    }

    private Menu createMenu(String name, Long parentId, String path, String component, String icon, int sortOrder, int type, int status) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setParentId(parentId);
        menu.setPath(path);
        menu.setComponent(component);
        menu.setIcon(icon);
        menu.setSortOrder(sortOrder);
        menu.setType(type);
        menu.setStatus(status);
        menu.setCreateTime(LocalDateTime.now());
        return menu;
    }

    private Role createRole(String name, String code, String description) {
        Role role = new Role();
        role.setName(name);
        role.setCode(code);
        role.setDescription(description);
        role.setStatus(1);
        role.setCreateTime(LocalDateTime.now());
        return role;
    }

    private User createUser(String username, String password, String realName, String phone, String email, Role role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        if (role != null) {
            user.setRoles(Arrays.asList(role));
        }
        return user;
    }

    private Product createProduct(String code, String name, String category, BigDecimal price, BigDecimal costPrice) {
        Product product = new Product();
        product.setProductCode(code);
        product.setProductName(name);
        product.setCategory(category);
        product.setDescription(name + "商品");
        product.setPrice(price);
        product.setCostPrice(costPrice);
        product.setStatus(1);
        product.setCreateTime(LocalDateTime.now());
        return product;
    }

    private Device createDevice(String deviceCode, String deviceName, String model, String firmwareVersion, 
                                 String location, BigDecimal lat, BigDecimal lng, String manager, String phone, Integer status) {
        Device device = new Device();
        device.setDeviceCode(deviceCode);
        device.setDeviceName(deviceName);
        device.setModel(model);
        device.setFirmwareVersion(firmwareVersion);
        device.setLocation(location);
        device.setLatitude(lat);
        device.setLongitude(lng);
        device.setManager(manager);
        device.setContactPhone(phone);
        device.setStatus(status);
        device.setInstallTime(LocalDateTime.now().minusDays(30));
        device.setLastReportTime(LocalDateTime.now());
        device.setCreateTime(LocalDateTime.now());
        return device;
    }

    private DeviceStatus createDeviceStatus(Device device) {
        DeviceStatus status = new DeviceStatus();
        status.setDeviceId(device.getId());
        status.setDeviceCode(device.getDeviceCode());
        status.setNetworkQuality((int) (Math.random() * 4) + 1);
        status.setSignalStrength((int) (Math.random() * -40) - 50);
        status.setMotorStatus(1);
        status.setDoorStatus(1);
        status.setTemperature(new BigDecimal(String.format("%.1f", Math.random() * 5 + 2)));
        status.setHumidity(new BigDecimal(String.format("%.1f", Math.random() * 30 + 50)));
        status.setPowerStatus(1);
        status.setBatteryLevel(100);
        status.setTargetTemperature(new BigDecimal("4.0"));
        status.setAdvertContent("欢迎使用自动售卖机！");
        status.setReportTime(LocalDateTime.now());
        status.setCreateTime(LocalDateTime.now());
        return status;
    }

    private Slot createSlot(Device device, int index, Product product) {
        String slotNo = "A" + String.format("%02d", index);
        int stock = (int) (Math.random() * 15) + 5;
        Slot slot = new Slot();
        slot.setDeviceId(device.getId());
        slot.setDeviceCode(device.getDeviceCode());
        slot.setDeviceName(device.getDeviceName());
        slot.setSlotNo(slotNo);
        slot.setSlotCode(slotNo);
        slot.setSlotIndex(index);
        slot.setProductId(product.getId());
        slot.setProductCode(product.getProductCode());
        slot.setProductName(product.getProductName());
        slot.setProductPrice(product.getPrice());
        slot.setProductUnit("件");
        slot.setCapacity(20);
        slot.setMaxCapacity(20);
        slot.setStock(stock);
        slot.setCurrentQuantity(stock);
        slot.setStatus(1);
        slot.setCreateTime(LocalDateTime.now());
        slot.setLastUpdateTime(LocalDateTime.now());
        return slot;
    }

    private Permission getPermissionByCode(List<Permission> permissions, String code) {
        return permissions.stream()
                .filter(p -> p.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

    private List<Menu> getMenusByPaths(List<Menu> menus, List<String> paths) {
        return menus.stream()
                .filter(m -> paths.contains(m.getPath()))
                .collect(java.util.stream.Collectors.toList());
    }
}
