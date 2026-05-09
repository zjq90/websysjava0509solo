package com.agriculture.service;

import com.agriculture.entity.Customer;
import com.agriculture.repository.CustomerRepository;
import com.agriculture.util.EncryptionUtil;
import com.agriculture.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 客户服务类
 * 提供客户管理的业务逻辑，敏感信息加密存储
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EncryptionUtil encryptionUtil;

    /**
     * 创建客户
     * 敏感信息（手机号、邮箱、联系人）采用AES-256加密存储
     * 
     * @param customer 客户信息
     * @return 创建后的客户
     */
    public Customer create(Customer customer) {
        ValidationUtil.ValidationResult phoneResult = ValidationUtil.validateChinaPhone(customer.getPhone());
        if (!phoneResult.isValid()) {
            throw new RuntimeException(phoneResult.getMessage());
        }

        ValidationUtil.ValidationResult emailResult = ValidationUtil.validateEmail(customer.getEmail());
        if (!emailResult.isValid()) {
            throw new RuntimeException(emailResult.getMessage());
        }

        if (customer.getCustomerCode() != null && customerRepository.existsByCustomerCode(customer.getCustomerCode())) {
            throw new RuntimeException("客户编号已存在");
        }

        encryptSensitiveFields(customer);

        return customerRepository.save(customer);
    }

    /**
     * 更新客户信息
     * 
     * @param customer 客户信息
     * @return 更新后的客户
     */
    public Customer update(Customer customer) {
        Optional<Customer> existingOpt = customerRepository.findById(customer.getId());
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("客户不存在");
        }

        Customer existing = existingOpt.get();

        if (customer.getCustomerCode() != null && 
            !customer.getCustomerCode().equals(existing.getCustomerCode()) &&
            customerRepository.existsByCustomerCode(customer.getCustomerCode())) {
            throw new RuntimeException("客户编号已存在");
        }

        if (customer.getPhone() != null) {
            ValidationUtil.ValidationResult phoneResult = ValidationUtil.validateChinaPhone(customer.getPhone());
            if (!phoneResult.isValid()) {
                throw new RuntimeException(phoneResult.getMessage());
            }
        }

        if (customer.getEmail() != null) {
            ValidationUtil.ValidationResult emailResult = ValidationUtil.validateEmail(customer.getEmail());
            if (!emailResult.isValid()) {
                throw new RuntimeException(emailResult.getMessage());
            }
        }

        encryptSensitiveFields(customer);

        return customerRepository.save(customer);
    }

    /**
     * 根据ID删除客户
     * 
     * @param id 客户ID
     */
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * 根据ID查找客户（敏感字段已解密）
     * 
     * @param id 客户ID
     * @return 客户对象
     */
    public Optional<Customer> findById(Long id) {
        Optional<Customer> customerOpt = customerRepository.findById(id);
        customerOpt.ifPresent(this::decryptSensitiveFields);
        return customerOpt;
    }

    /**
     * 根据客户编号查找
     * 
     * @param customerCode 客户编号
     * @return 客户对象
     */
    public Optional<Customer> findByCustomerCode(String customerCode) {
        Optional<Customer> customerOpt = customerRepository.findByCustomerCode(customerCode);
        customerOpt.ifPresent(this::decryptSensitiveFields);
        return customerOpt;
    }

    /**
     * 查询所有客户（敏感字段已解密）
     * 
     * @return 客户列表
     */
    public List<Customer> findAll() {
        List<Customer> customers = customerRepository.findAll();
        customers.forEach(this::decryptSensitiveFields);
        return customers;
    }

    /**
     * 查询启用状态的客户
     * 
     * @return 客户列表
     */
    public List<Customer> findActiveCustomers() {
        List<Customer> customers = customerRepository.findByStatusOrderByCreatedAtDesc("ACTIVE");
        customers.forEach(this::decryptSensitiveFields);
        return customers;
    }

    /**
     * 根据客户类型查询
     * 
     * @param customerType 客户类型
     * @return 客户列表
     */
    public List<Customer> findByCustomerType(String customerType) {
        List<Customer> customers = customerRepository.findByCustomerTypeOrderByCreatedAtDesc(customerType);
        customers.forEach(this::decryptSensitiveFields);
        return customers;
    }

    /**
     * 根据客户等级查询
     * 
     * @param customerLevel 客户等级
     * @return 客户列表
     */
    public List<Customer> findByCustomerLevel(String customerLevel) {
        List<Customer> customers = customerRepository.findByCustomerLevelAndStatus(customerLevel, "ACTIVE");
        customers.forEach(this::decryptSensitiveFields);
        return customers;
    }

    /**
     * 检查客户编号是否存在
     * 
     * @param customerCode 客户编号
     * @return 是否存在
     */
    public boolean existsByCustomerCode(String customerCode) {
        return customerRepository.existsByCustomerCode(customerCode);
    }

    /**
     * 加密敏感字段
     */
    private void encryptSensitiveFields(Customer customer) {
        if (customer.getPhone() != null) {
            customer.setPhone(encryptionUtil.encrypt(customer.getPhone()));
        }
        if (customer.getEmail() != null) {
            customer.setEmail(encryptionUtil.encrypt(customer.getEmail()));
        }
        if (customer.getContactPerson() != null) {
            customer.setContactPerson(encryptionUtil.encrypt(customer.getContactPerson()));
        }
    }

    /**
     * 解密敏感字段
     */
    private void decryptSensitiveFields(Customer customer) {
        if (customer.getPhone() != null) {
            customer.setPhone(encryptionUtil.decrypt(customer.getPhone()));
        }
        if (customer.getEmail() != null) {
            customer.setEmail(encryptionUtil.decrypt(customer.getEmail()));
        }
        if (customer.getContactPerson() != null) {
            customer.setContactPerson(encryptionUtil.decrypt(customer.getContactPerson()));
        }
    }
}
