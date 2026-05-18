package com.secondhand.config;

import com.secondhand.entity.*;
import com.secondhand.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DisputeRepository disputeRepository;

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initUsers();
        initProducts();
        initOrders();
        initDisputes();
        initOperationLogs();
    }

    private void initRoles() {
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("管理员");
            adminRole.setCode("ADMIN");
            adminRole.setDescription("系统管理员，拥有所有权限");
            adminRole.setPermissions("{\"product\":true,\"order\":true,\"dispute\":true,\"statistics\":true,\"system\":true}");
            roleRepository.save(adminRole);

            Role sellerRole = new Role();
            sellerRole.setName("卖家");
            sellerRole.setCode("SELLER");
            sellerRole.setDescription("商品卖家，管理商品和订单");
            sellerRole.setPermissions("{\"product\":true,\"order\":true,\"dispute\":true,\"statistics\":true,\"system\":false}");
            roleRepository.save(sellerRole);

            Role customerServiceRole = new Role();
            customerServiceRole.setName("客服");
            customerServiceRole.setCode("CUSTOMER_SERVICE");
            customerServiceRole.setDescription("客服人员，处理纠纷和订单问题");
            customerServiceRole.setPermissions("{\"product\":false,\"order\":true,\"dispute\":true,\"statistics\":true,\"system\":false}");
            roleRepository.save(customerServiceRole);

            Role buyerRole = new Role();
            buyerRole.setName("买家");
            buyerRole.setCode("BUYER");
            buyerRole.setDescription("普通买家，购买商品");
            buyerRole.setPermissions("{\"product\":false,\"order\":false,\"dispute\":false,\"statistics\":false,\"system\":false}");
            roleRepository.save(buyerRole);
        }
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByCode("ADMIN");
            Role sellerRole = roleRepository.findByCode("SELLER");
            Role buyerRole = roleRepository.findByCode("BUYER");

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setNickname("系统管理员");
            admin.setEmail("admin@example.com");
            admin.setPhone("13800138000");
            admin.setRoleId(adminRole.getId());
            admin.setStatus("ACTIVE");
            userRepository.save(admin);

            User seller = new User();
            seller.setUsername("seller");
            seller.setPassword("seller123");
            seller.setNickname("测试卖家");
            seller.setEmail("seller@example.com");
            seller.setPhone("13800138001");
            seller.setRoleId(sellerRole.getId());
            seller.setStatus("ACTIVE");
            userRepository.save(seller);

            User buyer = new User();
            buyer.setUsername("buyer");
            buyer.setPassword("buyer123");
            buyer.setNickname("测试买家");
            buyer.setEmail("buyer@example.com");
            buyer.setPhone("13800138002");
            buyer.setRoleId(buyerRole.getId());
            buyer.setStatus("ACTIVE");
            userRepository.save(buyer);
        }
    }

    private void initProducts() {
        if (productRepository.count() == 0) {
            User seller = userRepository.findByUsername("seller");

            String[] categories = {"电子产品", "服装", "家居", "图书", "运动"};
            String[] conditions = {"NEW", "LIKE_NEW", "EXCELLENT", "GOOD", "FAIR"};

            for (int i = 1; i <= 20; i++) {
                Product product = new Product();
                product.setName("测试商品" + i);
                product.setDescription("这是一个测试商品，商品编号" + i);
                product.setPrice(BigDecimal.valueOf(10 + i * 5));
                product.setStock(i <= 3 ? 5 : 50 + i);
                product.setCategory(categories[i % categories.length]);
                product.setCondition(conditions[i % conditions.length]);
                product.setStatus(i % 5 == 0 ? "OFF_SHELF" : "ON_SALE");
                product.setSellerId(seller.getId());
                productRepository.save(product);
            }
        }
    }

    private void initOrders() {
        if (orderRepository.count() == 0) {
            User seller = userRepository.findByUsername("seller");
            User buyer = userRepository.findByUsername("buyer");

            String[] statuses = {"PENDING_PAYMENT", "PENDING_SHIPMENT", "SHIPPED", "COMPLETED", "CANCELLED", "REFUNDED"};

            for (int i = 1; i <= 15; i++) {
                Order order = new Order();
                order.setOrderNo("ORD" + System.currentTimeMillis() + i);
                order.setProductId((long) (i % 10 + 1));
                order.setProductName("测试商品" + (i % 10 + 1));
                order.setQuantity(i);
                order.setTotalAmount(BigDecimal.valueOf(100 + i * 10));
                order.setBuyerId(buyer.getId());
                order.setSellerId(seller.getId());
                order.setStatus(statuses[i % statuses.length]);
                order.setIsAbnormal(i % 7 == 0);
                if (i % 7 == 0) {
                    order.setAbnormalReason("订单异常测试" + i);
                }
                order.setReceiverName("测试用户" + i);
                order.setReceiverPhone("13800" + String.format("%05d", i));
                order.setShippingAddress("测试地址" + i + "号");
                if (i % 3 == 0) {
                    order.setLogisticsCompany("顺丰快递");
                    order.setTrackingNumber("SF" + System.currentTimeMillis());
                }
                orderRepository.save(order);
            }
        }
    }

    private void initDisputes() {
        if (disputeRepository.count() == 0) {
            User seller = userRepository.findByUsername("seller");
            User buyer = userRepository.findByUsername("buyer");

            String[] types = {"GOODS_ISSUE", "PAYMENT_ISSUE", "SHIPPING_ISSUE", "OTHER"};
            String[] statuses = {"PENDING", "PROCESSING", "RESOLVED", "CLOSED"};

            for (int i = 1; i <= 5; i++) {
                Dispute dispute = new Dispute();
                dispute.setOrderId((long) i);
                dispute.setOrderNo("ORD" + System.currentTimeMillis() + i);
                dispute.setComplainantId(buyer.getId());
                dispute.setRespondentId(seller.getId());
                dispute.setType(types[i % types.length]);
                dispute.setTitle("纠纷标题测试" + i);
                dispute.setDescription("这是一个测试纠纷描述" + i);
                dispute.setStatus(statuses[i % statuses.length]);
                if (i % 2 == 0) {
                    dispute.setArbitrationResult("仲裁结果测试" + i);
                }
                disputeRepository.save(dispute);
            }
        }
    }

    private void initOperationLogs() {
        if (operationLogRepository.count() == 0) {
            User admin = userRepository.findByUsername("admin");

            String[] modules = {"商品管理", "订单管理", "纠纷管理", "系统管理"};
            String[] types = {"CREATE", "UPDATE", "DELETE", "QUERY", "EXPORT", "IMPORT"};

            for (int i = 1; i <= 10; i++) {
                OperationLog log = new OperationLog();
                log.setOperatorId(admin.getId());
                log.setOperatorName(admin.getNickname());
                log.setModule(modules[i % modules.length]);
                log.setType(types[i % types.length]);
                log.setDescription("测试操作日志" + i);
                log.setResult("SUCCESS");
                log.setIpAddress("192.168.1." + i);
                operationLogRepository.save(log);
            }
        }
    }

}