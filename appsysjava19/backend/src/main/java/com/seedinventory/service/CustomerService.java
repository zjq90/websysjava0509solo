package com.seedinventory.service;

import com.seedinventory.entity.Customer;
import com.seedinventory.repository.CustomerRepository;
import com.seedinventory.util.AES256Util;
import com.seedinventory.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * 客户管理服务类
 * 客户敏感数据（手机号、地址等）采用AES-256加密存储
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Service
public class CustomerService {
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Value("${app.encryption.secret-key}")
    private String secretKey;
    
    @Value("${app.encryption.init-vector}")
    private String initVector;
    
    /**
     * 新增客户
     * 敏感数据自动加密
     */
    @Transactional(rollbackFor = Exception.class)
    public Customer create(Customer customer) {
        ValidationUtil.ValidationResult phoneResult = ValidationUtil.validatePhone(decryptIfNeeded(customer.getPhone()));
        if (!phoneResult.isSuccess()) {
            throw new RuntimeException(phoneResult.getMessage());
        }
        
        try {
            customer.setPhone(encrypt(customer.getPhone()));
            if (customer.getEmail() != null && !customer.getEmail().isEmpty()) {
                customer.setEmail(encrypt(customer.getEmail()));
            }
            if (customer.getAddress() != null && !customer.getAddress().isEmpty()) {
                customer.setAddress(encrypt(customer.getAddress()));
            }
        } catch (Exception e) {
            throw new RuntimeException("数据加密失败：" + e.getMessage());
        }
        
        return customerRepository.save(customer);
    }
    
    /**
     * 更新客户信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Customer update(Customer customer) {
        if (customer.getPhone() != null) {
            ValidationUtil.ValidationResult phoneResult = ValidationUtil.validatePhone(customer.getPhone());
            if (!phoneResult.isSuccess()) {
                throw new RuntimeException(phoneResult.getMessage());
            }
            try {
                customer.setPhone(encrypt(customer.getPhone()));
            } catch (Exception e) {
                throw new RuntimeException("数据加密失败：" + e.getMessage());
            }
        }
        
        return customerRepository.save(customer);
    }
    
    /**
     * 根据ID查询客户（返回解密后的数据）
     */
    public Optional<Customer> findById(Long id) {
        Optional<Customer> opt = customerRepository.findById(id);
        if (opt.isPresent()) {
            return Optional.of(decryptCustomer(opt.get()));
        }
        return opt;
    }
    
    /**
     * 查询所有客户（返回解密后的数据）
     */
    public List<Customer> findAll() {
        List<Customer> list = customerRepository.findAll();
        for (Customer customer : list) {
            decryptCustomer(customer);
        }
        return list;
    }
    
    /**
     * 根据客户编号查询
     */
    public Optional<Customer> findByCustomerCode(String customerCode) {
        Optional<Customer> opt = customerRepository.findByCustomerCode(customerCode);
        if (opt.isPresent()) {
            return Optional.of(decryptCustomer(opt.get()));
        }
        return opt;
    }
    
    /**
     * 删除客户
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
    
    /**
     * 解密客户敏感信息
     */
    private Customer decryptCustomer(Customer customer) {
        try {
            customer.setPhone(decrypt(customer.getPhone()));
            if (customer.getEmail() != null) {
                customer.setEmail(decrypt(customer.getEmail()));
            }
            if (customer.getAddress() != null) {
                customer.setAddress(decrypt(customer.getAddress()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }
    
    /**
     * 加密数据
     */
    private String encrypt(String plainText) throws Exception {
        if (plainText == null || plainText.isEmpty()) {
            return plainText;
        }
        return AES256Util.encrypt(plainText, secretKey, initVector);
    }
    
    /**
     * 解密数据
     */
    private String decrypt(String encryptedText) throws Exception {
        if (encryptedText == null || encryptedText.isEmpty()) {
            return encryptedText;
        }
        return AES256Util.decrypt(encryptedText, secretKey, initVector);
    }
    
    /**
     * 检查是否已加密（简单检查Base64格式）
     */
    private String decryptIfNeeded(String text) {
        if (text == null) return null;
        try {
            return decrypt(text);
        } catch (Exception e) {
            return text;
        }
    }
}
