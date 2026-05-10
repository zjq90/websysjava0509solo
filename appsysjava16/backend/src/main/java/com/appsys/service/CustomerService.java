package com.appsys.service;

import com.appsys.entity.Customer;
import com.appsys.entity.CustomerLevel;
import com.appsys.entity.CustomerVisitRecord;
import com.appsys.entity.SalesOrder;
import com.appsys.exception.ResourceNotFoundException;
import com.appsys.repository.CustomerRepository;
import com.appsys.repository.CustomerVisitRecordRepository;
import com.appsys.repository.SalesOrderRepository;
import com.appsys.util.AesEncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 客户服务类
 * 处理客户的增删改查及相关业务逻辑
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    
    @Autowired
    private CustomerVisitRecordRepository visitRecordRepository;

    /**
     * 获取所有客户
     * @return 客户列表
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllOrderByCreatedAtDesc();
    }

    /**
     * 根据ID获取客户
     * @param id 客户ID
     * @return 客户
     */
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("客户不存在，ID: " + id));
    }

    /**
     * 创建客户
     * @param customer 客户信息
     * @return 创建后的客户
     */
    @Transactional
    public Customer createCustomer(Customer customer) {
        String rawPhone = customer.getPhone();
        String rawAddress = customer.getAddress();
        
        if (rawPhone != null) {
            customer.setPhone(AesEncryptionUtil.Encryptor.encrypt(rawPhone));
        }
        if (rawAddress != null) {
            customer.setAddress(AesEncryptionUtil.Encryptor.encrypt(rawAddress));
        }
        
        if (customer.getLevel() == null) {
            customer.setLevel(CustomerLevel.TEMPORARY);
        }
        
        return customerRepository.save(customer);
    }

    /**
     * 创建临时客户（展会或下乡推广时快速注册）
     * @param name 客户姓名
     * @param phone 手机号
     * @return 创建的客户
     */
    @Transactional
    public Customer createTemporaryCustomer(String name, String phone) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setPhone(AesEncryptionUtil.Encryptor.encrypt(phone));
        customer.setLevel(CustomerLevel.TEMPORARY);
        customer.setIsTemporary(true);
        customer.setCreditLimit(BigDecimal.ZERO);
        customer.setUsedCredit(BigDecimal.ZERO);
        customer.setTotalPurchaseAmount(BigDecimal.ZERO);
        
        return customerRepository.save(customer);
    }

    /**
     * 更新客户
     * @param id 客户ID
     * @param customerDetails 更新的客户信息
     * @return 更新后的客户
     */
    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = getCustomerById(id);
        
        customer.setName(customerDetails.getName());
        
        if (customerDetails.getPhone() != null && !customerDetails.getPhone().isEmpty()) {
            customer.setPhone(AesEncryptionUtil.Encryptor.encrypt(customerDetails.getPhone()));
        }
        
        if (customerDetails.getAddress() != null && !customerDetails.getAddress().isEmpty()) {
            customer.setAddress(AesEncryptionUtil.Encryptor.encrypt(customerDetails.getAddress()));
        }
        
        if (customerDetails.getCreditLimit() != null) {
            customer.setCreditLimit(customerDetails.getCreditLimit());
        }
        
        if (customerDetails.getRemarks() != null) {
            customer.setRemarks(customerDetails.getRemarks());
        }
        
        if (customerDetails.getIsTemporary() != null) {
            customer.setIsTemporary(customerDetails.getIsTemporary());
        }
        
        return customerRepository.save(customer);
    }

    /**
     * 删除客户
     * @param id 客户ID
     */
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = getCustomerById(id);
        customerRepository.delete(customer);
    }

    /**
     * 根据名称搜索客户
     * @param name 名称关键字
     * @return 客户列表
     */
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContaining(name);
    }

    /**
     * 获取客户历史订单
     * @param customerId 客户ID
     * @return 订单列表
     */
    public List<SalesOrder> getCustomerHistoryOrders(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return salesOrderRepository.findCustomerHistoryOrders(customerId);
    }

    /**
     * 获取客户回访记录
     * @param customerId 客户ID
     * @return 回访记录列表
     */
    public List<CustomerVisitRecord> getCustomerVisitRecords(Long customerId) {
        Customer customer = getCustomerById(customerId);
        return visitRecordRepository.findByCustomerIdOrderByVisitTimeDesc(customerId);
    }

    /**
     * 添加客户回访记录
     * @param customerId 客户ID
     * @param record 回访记录
     * @return 回访记录
     */
    @Transactional
    public CustomerVisitRecord addVisitRecord(Long customerId, CustomerVisitRecord record) {
        Customer customer = getCustomerById(customerId);
        record.setCustomerId(customerId);
        return visitRecordRepository.save(record);
    }

    /**
     * 更新客户累计消费金额并自动调整等级
     * @param customerId 客户ID
     * @param amount 新增消费金额
     * @return 更新后的客户
     */
    @Transactional
    public Customer updatePurchaseAmount(Long customerId, BigDecimal amount) {
        Customer customer = getCustomerById(customerId);
        
        BigDecimal newTotal = customer.getTotalPurchaseAmount().add(amount);
        customer.setTotalPurchaseAmount(newTotal);
        
        CustomerLevel newLevel = CustomerLevel.calculateLevel(newTotal);
        customer.setLevel(newLevel);
        
        return customerRepository.save(customer);
    }

    /**
     * 获取临时客户列表
     * @return 临时客户列表
     */
    public List<Customer> getTemporaryCustomers() {
        return customerRepository.findByIsTemporaryTrue();
    }

    /**
     * 获取正式客户列表
     * @return 正式客户列表
     */
    public List<Customer> getRegularCustomers() {
        return customerRepository.findByIsTemporaryFalse();
    }

    /**
     * 获取消费排行前N的客户
     * @param limit 数量
     * @return 客户列表
     */
    public List<Customer> getTopCustomers(int limit) {
        return customerRepository.findTopCustomers(limit);
    }
}
