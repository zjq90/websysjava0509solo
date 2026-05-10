package com.appsys.finance.config;

import com.appsys.finance.entity.Customer;
import com.appsys.finance.entity.Employee;
import com.appsys.finance.entity.Product;
import com.appsys.finance.entity.SalesOrder;
import com.appsys.finance.repository.CustomerRepository;
import com.appsys.finance.repository.EmployeeRepository;
import com.appsys.finance.repository.ProductRepository;
import com.appsys.finance.repository.SalesOrderRepository;
import com.appsys.finance.util.AesEncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private AesEncryptUtil aesEncryptUtil;

    @Override
    public void run(String... args) {
        initCustomers();
        initEmployees();
        initProducts();
        initSalesOrders();
    }

    private void initCustomers() {
        String[] names = {"张三", "李四", "王五", "赵六", "钱七"};
        String[] phones = {"13800138001", "13900139002", "13700137003", "13600136004", "13500135005"};
        String[] addresses = {"北京市朝阳区", "上海市浦东新区", "广州市天河区", "深圳市南山区", "杭州市西湖区"};

        for (int i = 0; i < names.length; i++) {
            Customer customer = new Customer();
            customer.setName(names[i]);
            customer.setPhone(aesEncryptUtil.encrypt(phones[i]));
            customer.setEmail(aesEncryptUtil.encrypt("customer" + (i + 1) + "@example.com"));
            customer.setAddress(addresses[i]);
            customer.setRemark("测试客户" + (i + 1));
            customerRepository.save(customer);
        }
    }

    private void initEmployees() {
        Employee emp1 = new Employee();
        emp1.setName("王经理");
        emp1.setEmployeeNo("EMP001");
        emp1.setPhone(aesEncryptUtil.encrypt("18900189001"));
        emp1.setDepartment("销售部");
        emp1.setPosition("销售经理");
        emp1.setCommissionRate(new BigDecimal("3.5"));
        emp1.setIsManager(true);
        employeeRepository.save(emp1);

        Employee emp2 = new Employee();
        emp2.setName("销售员A");
        emp2.setEmployeeNo("EMP002");
        emp2.setPhone(aesEncryptUtil.encrypt("18900189002"));
        emp2.setDepartment("销售部");
        emp2.setPosition("销售员");
        emp2.setCommissionRate(new BigDecimal("2.0"));
        emp2.setIsManager(false);
        employeeRepository.save(emp2);

        Employee emp3 = new Employee();
        emp3.setName("销售员B");
        emp3.setEmployeeNo("EMP003");
        emp3.setPhone(aesEncryptUtil.encrypt("18900189003"));
        emp3.setDepartment("销售部");
        emp3.setPosition("销售员");
        emp3.setCommissionRate(new BigDecimal("2.5"));
        emp3.setIsManager(false);
        employeeRepository.save(emp3);
    }

    private void initProducts() {
        LocalDate minExpiry = LocalDate.now().plusMonths(6);

        Product p1 = new Product();
        p1.setName("优质小麦种子");
        p1.setBatchNumber("BAT001A1");
        p1.setExpiryDate(minExpiry.plusMonths(12));
        p1.setGerminationRate(new BigDecimal("95.5"));
        p1.setUnitPrice(new BigDecimal("120.00"));
        p1.setCostPrice(new BigDecimal("80.00"));
        p1.setStockQuantity(1000);
        productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("高产玉米种子");
        p2.setBatchNumber("BAT002B2");
        p2.setExpiryDate(minExpiry.plusMonths(18));
        p2.setGerminationRate(new BigDecimal("92.3"));
        p2.setUnitPrice(new BigDecimal("85.50"));
        p2.setCostPrice(new BigDecimal("55.00"));
        p2.setStockQuantity(2000);
        productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("有机蔬菜种子");
        p3.setBatchNumber("BAT003C3");
        p3.setExpiryDate(minExpiry.plusMonths(9));
        p3.setGerminationRate(new BigDecimal("88.8"));
        p3.setUnitPrice(new BigDecimal("50.00"));
        p3.setCostPrice(new BigDecimal("30.00"));
        p3.setStockQuantity(5000);
        productRepository.save(p3);

        Product p4 = new Product();
        p4.setName("大豆种子");
        p4.setBatchNumber("BAT004D4");
        p4.setExpiryDate(minExpiry.plusMonths(24));
        p4.setGerminationRate(new BigDecimal("90.0"));
        p4.setUnitPrice(new BigDecimal("68.00"));
        p4.setCostPrice(new BigDecimal("45.00"));
        p4.setStockQuantity(1500);
        productRepository.save(p4);
    }

    private void initSalesOrders() {
        Customer c1 = customerRepository.findById(1L).orElse(null);
        Customer c2 = customerRepository.findById(2L).orElse(null);
        Customer c3 = customerRepository.findById(3L).orElse(null);

        Employee emp2 = employeeRepository.findById(2L).orElse(null);
        Employee emp3 = employeeRepository.findById(3L).orElse(null);

        Product p1 = productRepository.findById(1L).orElse(null);
        Product p2 = productRepository.findById(2L).orElse(null);
        Product p3 = productRepository.findById(3L).orElse(null);

        if (c1 != null && emp2 != null && p1 != null) {
            SalesOrder order1 = new SalesOrder();
            order1.setOrderNo("ORDTEST01");
            order1.setCustomer(c1);
            order1.setProduct(p1);
            order1.setEmployee(emp2);
            order1.setQuantity(100);
            order1.setUnitPrice(new BigDecimal("120.00"));
            order1.setTotalAmount(new BigDecimal("12000.00"));
            order1.setPaidAmount(new BigDecimal("12000.00"));
            order1.setCommissionAmount(new BigDecimal("240.00"));
            order1.setStatus("COMPLETED");
            salesOrderRepository.save(order1);
        }

        if (c2 != null && emp3 != null && p2 != null) {
            SalesOrder order2 = new SalesOrder();
            order2.setOrderNo("ORDTEST02");
            order2.setCustomer(c2);
            order2.setProduct(p2);
            order2.setEmployee(emp3);
            order2.setQuantity(200);
            order2.setUnitPrice(new BigDecimal("85.50"));
            order2.setTotalAmount(new BigDecimal("17100.00"));
            order2.setPaidAmount(new BigDecimal("10000.00"));
            order2.setCommissionAmount(new BigDecimal("250.00"));
            order2.setStatus("PARTIAL");
            salesOrderRepository.save(order2);
        }

        if (c3 != null && emp2 != null && p3 != null) {
            SalesOrder order3 = new SalesOrder();
            order3.setOrderNo("ORDTEST03");
            order3.setCustomer(c3);
            order3.setProduct(p3);
            order3.setEmployee(emp2);
            order3.setQuantity(500);
            order3.setUnitPrice(new BigDecimal("50.00"));
            order3.setTotalAmount(new BigDecimal("25000.00"));
            order3.setPaidAmount(new BigDecimal("25000.00"));
            order3.setCommissionAmount(new BigDecimal("500.00"));
            order3.setStatus("COMPLETED");
            salesOrderRepository.save(order3);
        }
    }
}
