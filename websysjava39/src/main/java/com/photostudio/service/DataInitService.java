package com.photostudio.service;

import com.photostudio.entity.*;
import com.photostudio.entity.Package;
import com.photostudio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 数据初始化服务
 * 生成测试数据
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Service
public class DataInitService {

    @Autowired
    private StoreRepository storeRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private PackageRepository packageRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private FinanceRecordRepository financeRecordRepository;
    
    @Autowired
    private ReimbursementRepository reimbursementRepository;
    
    @Autowired
    private PhotoEditRecordRepository photoEditRecordRepository;
    
    private Random random = new Random();
    
    private String[] storeNames = {"总店", "朝阳门店", "海淀门店", "西城门店"};
    private String[] employeeNames = {"张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十"};
    private String[] positions = {"SALES", "PHOTOGRAPHER", "MAKEUP_ARTIST", "PHOTO_SELECTOR", "PHOTO_EDITOR", "MANAGER"};
    private String[] packageTypes = {"WEDDING", "PORTRAIT", "FAMILY", "CHILDREN", "COMMERCIAL"};
    private String[] packageNames = {
        "经典婚纱套餐", "高端婚纱套餐", "个人写真套餐", "全家福套餐", 
        "儿童成长套餐", "商业摄影套餐", "外景婚纱套餐", "室内写真套餐"
    };
    private String[] customerNames = {"顾客A", "顾客B", "顾客C", "顾客D", "顾客E", "顾客F", "顾客G", "顾客H", "顾客I", "顾客J"};
    private String[] orderStatuses = {"CONSULTING", "ORDERED", "SHOOTING", "SHOOT_COMPLETED", "SELECTING", "EDITING", "DELIVERED"};

    /**
     * 初始化所有测试数据
     */
    @Transactional
    public void initAllData() {
        // 1. 创建门店
        List<Store> stores = createStores();
        
        // 2. 创建员工
        List<Employee> employees = createEmployees(stores);
        
        // 3. 创建套餐
        List<Package> packages = createPackages();
        
        // 4. 创建客户
        List<Customer> customers = createCustomers();
        
        // 5. 创建订单
        List<Order> orders = createOrders(stores, employees, packages, customers);
        
        // 6. 创建收支记录
        createFinanceRecords(stores, orders);
        
        // 7. 创建报销记录
        createReimbursements(employees);
        
        // 8. 创建修图记录
        createPhotoEditRecords(orders, employees);
    }
    
    /**
     * 创建门店数据
     */
    private List<Store> createStores() {
        List<Store> stores = new ArrayList<>();
        for (int i = 0; i < storeNames.length; i++) {
            Store store = new Store();
            store.setName(storeNames[i]);
            store.setAddress("北京市XX区XX街道" + (i + 1) + "号");
            store.setPhone("010-1234567" + i);
            store.setManager(employeeNames[i]);
            store.setMonthlyTarget(BigDecimal.valueOf(100000 + random.nextInt(50000)));
            store.setActive(true);
            store.setCreateTime(LocalDateTime.now());
            store.setUpdateTime(LocalDateTime.now());
            stores.add(storeRepository.save(store));
        }
        return stores;
    }
    
    /**
     * 创建员工数据
     */
    private List<Employee> createEmployees(List<Store> stores) {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < employeeNames.length; i++) {
            Employee employee = new Employee();
            employee.setName(employeeNames[i]);
            employee.setPhone("1380013800" + i);
            employee.setPosition(positions[i % positions.length]);
            employee.setStore(stores.get(i % stores.size()));
            employee.setHireDate(LocalDate.now().minusMonths(random.nextInt(24)));
            employee.setBaseSalary(BigDecimal.valueOf(5000 + random.nextInt(10000)));
            employee.setTotalOrders(random.nextInt(50));
            employee.setTotalRevenue(BigDecimal.valueOf(random.nextInt(100000)));
            employee.setAverageRating(BigDecimal.valueOf(3 + random.nextDouble() * 2));
            employee.setActive(true);
            employee.setCreateTime(LocalDateTime.now());
            employee.setUpdateTime(LocalDateTime.now());
            employees.add(employeeRepository.save(employee));
        }
        return employees;
    }
    
    /**
     * 创建套餐数据
     */
    private List<Package> createPackages() {
        List<Package> packages = new ArrayList<>();
        BigDecimal[] prices = {BigDecimal.valueOf(2999), BigDecimal.valueOf(5999), BigDecimal.valueOf(1299), 
                              BigDecimal.valueOf(1999), BigDecimal.valueOf(999), BigDecimal.valueOf(8999),
                              BigDecimal.valueOf(7999), BigDecimal.valueOf(1599)};
        
        for (int i = 0; i < packageNames.length; i++) {
            Package pkg = new Package();
            pkg.setName(packageNames[i]);
            pkg.setType(packageTypes[i % packageTypes.length]);
            pkg.setDescription(packageNames[i] + "详细描述...");
            pkg.setPrice(prices[i]);
            pkg.setCostLabor(prices[i].multiply(BigDecimal.valueOf(0.3)));
            pkg.setCostClothing(prices[i].multiply(BigDecimal.valueOf(0.1)));
            pkg.setCostMaterials(prices[i].multiply(BigDecimal.valueOf(0.05)));
            pkg.setTotalOrders(random.nextInt(30));
            pkg.setTotalRevenue(BigDecimal.valueOf(random.nextInt(50000)));
            pkg.setActive(true);
            pkg.setCreateTime(LocalDateTime.now());
            pkg.setUpdateTime(LocalDateTime.now());
            packages.add(packageRepository.save(pkg));
        }
        return packages;
    }
    
    /**
     * 创建客户数据
     */
    private List<Customer> createCustomers() {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < customerNames.length; i++) {
            Customer customer = new Customer();
            customer.setName(customerNames[i]);
            customer.setPhone("1390013900" + i);
            customer.setGender(i % 2 == 0 ? "男" : "女");
            customer.setBirthDate(LocalDate.now().minusYears(20 + random.nextInt(20)));
            customer.setAddress("北京市XX区XX小区" + (i + 1) + "号楼");
            customer.setWechat("wechat_" + i);
            if (i > 0 && random.nextBoolean()) {
                customer.setReferral(customers.get(random.nextInt(i)));
            }
            customer.setOldCustomer(random.nextBoolean());
            customer.setTotalOrders(random.nextInt(5));
            customer.setTotalAmount(BigDecimal.valueOf(random.nextInt(20000)));
            customer.setLastVisitTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            customer.setCreateTime(LocalDateTime.now().minusMonths(random.nextInt(12)));
            customer.setUpdateTime(LocalDateTime.now());
            customers.add(customerRepository.save(customer));
        }
        return customers;
    }
    
    /**
     * 创建订单数据
     */
    private List<Order> createOrders(List<Store> stores, List<Employee> employees, 
                                     List<Package> packages, List<Customer> customers) {
        List<Employee> salesList = employees.stream().filter(e -> "SALES".equals(e.getPosition())).collect(Collectors.toList());
        List<Employee> photographerList = employees.stream().filter(e -> "PHOTOGRAPHER".equals(e.getPosition())).collect(Collectors.toList());
        List<Employee> makeupArtistList = employees.stream().filter(e -> "MAKEUP_ARTIST".equals(e.getPosition())).collect(Collectors.toList());
        List<Employee> photoSelectorList = employees.stream().filter(e -> "PHOTO_SELECTOR".equals(e.getPosition())).collect(Collectors.toList());
        List<Employee> photoEditorList = employees.stream().filter(e -> "PHOTO_EDITOR".equals(e.getPosition())).collect(Collectors.toList());
        
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Order order = new Order();
            order.setOrderNo("ORD" + System.currentTimeMillis() + i);
            order.setCustomer(customers.get(random.nextInt(customers.size())));
            Package pkg = packages.get(random.nextInt(packages.size()));
            order.setAPackage(pkg);
            order.setStore(stores.get(random.nextInt(stores.size())));
            
            if (!salesList.isEmpty()) order.setSales(salesList.get(random.nextInt(salesList.size())));
            if (!photographerList.isEmpty()) order.setPhotographer(photographerList.get(random.nextInt(photographerList.size())));
            if (!makeupArtistList.isEmpty()) order.setMakeupArtist(makeupArtistList.get(random.nextInt(makeupArtistList.size())));
            if (!photoSelectorList.isEmpty()) order.setPhotoSelector(photoSelectorList.get(random.nextInt(photoSelectorList.size())));
            if (!photoEditorList.isEmpty()) order.setPhotoEditor(photoEditorList.get(random.nextInt(photoEditorList.size())));
            
            String status = orderStatuses[random.nextInt(orderStatuses.length)];
            order.setStatus(status);
            order.setAmount(pkg.getPrice());
            order.setDepositAmount(pkg.getPrice().multiply(BigDecimal.valueOf(0.3)));
            order.setBalanceAmount(pkg.getPrice().multiply(BigDecimal.valueOf(0.7)));
            
            LocalDateTime createTime = LocalDateTime.now().minusDays(random.nextInt(60));
            order.setShootDate(createTime.toLocalDate().plusDays(random.nextInt(10)));
            if ("DELIVERED".equals(status)) {
                order.setDeliverDate(order.getShootDate().plusDays(random.nextInt(15) + 7));
            }
            
            order.setCustomerRating(BigDecimal.valueOf(3 + random.nextDouble() * 2));
            order.setCostLabor(pkg.getCostLabor());
            order.setCostClothing(pkg.getCostClothing());
            order.setCostMaterials(pkg.getCostMaterials());
            
            order.setConsultTime(createTime);
            if (!"CONSULTING".equals(status)) {
                order.setOrderTime(createTime.plusHours(random.nextInt(48)));
            }
            if ("SHOOT_COMPLETED".equals(status) || "SELECTING".equals(status) || 
                "EDITING".equals(status) || "DELIVERED".equals(status)) {
                order.setShootTime(createTime.plusDays(random.nextInt(10)));
            }
            if ("DELIVERED".equals(status)) {
                order.setDeliverTime(createTime.plusDays(random.nextInt(30)));
            }
            
            order.setCreateTime(createTime);
            order.setUpdateTime(LocalDateTime.now());
            
            orders.add(orderRepository.save(order));
        }
        return orders;
    }
    
    /**
     * 创建收支记录
     */
    private void createFinanceRecords(List<Store> stores, List<Order> orders) {
        // 收入记录
        for (int i = 0; i < 30; i++) {
            FinanceRecord record = new FinanceRecord();
            record.setRecordNo("FIN" + System.currentTimeMillis() + i);
            record.setType("INCOME");
            record.setCategory("ORDER_PAYMENT");
            record.setAmount(BigDecimal.valueOf(1000 + random.nextInt(9000)));
            record.setStore(stores.get(random.nextInt(stores.size())));
            if (i < orders.size()) {
                record.setOrder(orders.get(i));
            }
            record.setRemark("订单收款");
            record.setRecordDate(LocalDate.now().minusDays(random.nextInt(30)));
            record.setCreateTime(LocalDateTime.now());
            financeRecordRepository.save(record);
        }
        
        // 支出记录
        String[] expenseCategories = {"SALARY", "RENT", "MATERIALS", "OTHER"};
        String[] expenseRemarks = {"员工工资", "房租", "耗材采购", "其他支出"};
        
        for (int i = 0; i < 20; i++) {
            FinanceRecord record = new FinanceRecord();
            record.setRecordNo("FIN" + System.currentTimeMillis() + (i + 100));
            record.setType("EXPENSE");
            int idx = random.nextInt(expenseCategories.length);
            record.setCategory(expenseCategories[idx]);
            record.setAmount(BigDecimal.valueOf(500 + random.nextInt(5000)));
            record.setStore(stores.get(random.nextInt(stores.size())));
            record.setRemark(expenseRemarks[idx]);
            record.setRecordDate(LocalDate.now().minusDays(random.nextInt(30)));
            record.setCreateTime(LocalDateTime.now());
            financeRecordRepository.save(record);
        }
    }
    
    /**
     * 创建报销记录
     */
    private void createReimbursements(List<Employee> employees) {
        String[] reimburseCategories = {"TRAVEL", "TRANSPORT", "MEAL", "OFFICE", "OTHER"};
        String[] reimburseTitles = {"差旅费报销", "交通费报销", "餐费报销", "办公用品报销", "其他报销"};
        String[] statuses = {"PENDING", "APPROVED", "REJECTED"};
        
        for (int i = 0; i < 15; i++) {
            Reimbursement reimburse = new Reimbursement();
            reimburse.setReimburseNo("REIMB" + System.currentTimeMillis() + i);
            reimburse.setEmployee(employees.get(random.nextInt(employees.size())));
            int idx = random.nextInt(reimburseCategories.length);
            reimburse.setTitle(reimburseTitles[idx]);
            reimburse.setDescription(reimburseTitles[idx] + "详细说明");
            reimburse.setCategory(reimburseCategories[idx]);
            reimburse.setAmount(BigDecimal.valueOf(50 + random.nextInt(1000)));
            reimburse.setVoucherUrl("http://example.com/voucher/" + i + ".jpg");
            reimburse.setStatus(statuses[random.nextInt(statuses.length)]);
            if (!"PENDING".equals(reimburse.getStatus())) {
                reimburse.setApprover(employees.get(random.nextInt(employees.size())));
                reimburse.setApproveTime(LocalDateTime.now());
                reimburse.setApproveRemark("已" + ("APPROVED".equals(reimburse.getStatus()) ? "批准" : "拒绝"));
            }
            reimburse.setCreateTime(LocalDateTime.now().minusDays(random.nextInt(15)));
            reimburse.setUpdateTime(LocalDateTime.now());
            reimbursementRepository.save(reimburse);
        }
    }
    
    /**
     * 创建修图记录
     */
    private void createPhotoEditRecords(List<Order> orders, List<Employee> employees) {
        List<Employee> photoEditorList = employees.stream().filter(e -> "PHOTO_EDITOR".equals(e.getPosition())).collect(Collectors.toList());
        
        for (int i = 0; i < 30; i++) {
            PhotoEditRecord record = new PhotoEditRecord();
            record.setOrder(orders.get(random.nextInt(orders.size())));
            record.setEditor(photoEditorList.get(random.nextInt(photoEditorList.size())));
            record.setPhotoCount(20 + random.nextInt(50));
            int reworkCount = random.nextInt(5);
            record.setReworkCount(reworkCount);
            record.setReworked(reworkCount > 0);
            record.setStartTime(LocalDateTime.now().minusDays(random.nextInt(10)));
            record.setCompleteTime(record.getStartTime().plusDays(random.nextInt(3) + 1));
            record.setRemark("修图完成");
            record.setCreateTime(LocalDateTime.now());
            photoEditRecordRepository.save(record);
        }
    }
}
