package com.photostudio.service;

import com.photostudio.entity.Customer;
import com.photostudio.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 客户服务层
 * 处理客户相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 创建客户
     */
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByPhoneAndDeletedFalse(customer.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }
        return customerRepository.save(customer);
    }

    /**
     * 更新客户信息
     */
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("客户不存在"));
        
        if (!customer.getPhone().equals(customerDetails.getPhone()) &&
            customerRepository.existsByPhoneAndDeletedFalse(customerDetails.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }

        customer.setName(customerDetails.getName());
        customer.setPhone(customerDetails.getPhone());
        customer.setBirthday(customerDetails.getBirthday());
        customer.setFamilyMembers(customerDetails.getFamilyMembers());
        customer.setPhotoType(customerDetails.getPhotoType());
        customer.setPreference(customerDetails.getPreference());
        customer.setLifecycle(customerDetails.getLifecycle());

        return customerRepository.save(customer);
    }

    /**
     * 删除客户（软删除）
     */
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("客户不存在"));
        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    /**
     * 根据ID查询客户
     */
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id).filter(c -> !c.getDeleted());
    }

    /**
     * 查询所有客户
     */
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAllByDeletedFalse();
    }

    /**
     * 根据生命周期阶段查询客户
     */
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByLifecycle(Customer.CustomerLifecycle lifecycle) {
        return customerRepository.findByLifecycleAndDeletedFalse(lifecycle);
    }

    /**
     * 根据姓名模糊查询客户
     */
    @Transactional(readOnly = true)
    public List<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingAndDeletedFalse(name);
    }

    /**
     * 根据手机号查询客户
     */
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByPhone(String phone) {
        return customerRepository.findByPhoneAndDeletedFalse(phone);
    }

    /**
     * 更新客户生命周期阶段
     */
    public Customer updateLifecycle(Long id, Customer.CustomerLifecycle lifecycle) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("客户不存在"));
        customer.setLifecycle(lifecycle);
        return customerRepository.save(customer);
    }

    /**
     * 根据拍摄类型查询客户
     */
    @Transactional(readOnly = true)
    public List<Customer> getCustomersByPhotoType(Customer.PhotoType photoType) {
        return customerRepository.findByPhotoTypeAndDeletedFalse(photoType);
    }
}
