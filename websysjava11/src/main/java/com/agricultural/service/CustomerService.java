package com.agricultural.service;

import com.agricultural.entity.Customer;
import com.agricultural.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 客户/供应商管理服务类
 * 提供客户和供应商的增删改查功能
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * 查询所有客户/供应商
     */
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * 分页查询所有客户/供应商
     */
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    /**
     * 查询所有启用的客户/供应商
     */
    public List<Customer> findAllEnabled() {
        return customerRepository.findByEnabledTrue();
    }

    /**
     * 根据ID查询
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * 根据客户编号查询
     */
    public Optional<Customer> findByCode(String code) {
        return customerRepository.findByCustomerCode(code);
    }

    /**
     * 按类型查询
     */
    public List<Customer> findByType(String type) {
        return customerRepository.findByCustomerTypeAndEnabledTrue(type);
    }

    /**
     * 根据名称模糊查询
     */
    public List<Customer> searchByName(String name) {
        return customerRepository.findByCustomerNameContaining(name);
    }

    /**
     * 保存客户/供应商
     */
    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * 根据ID删除
     */
    @Transactional
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * 检查客户编号是否存在
     */
    public boolean existsByCode(String code) {
        return customerRepository.existsByCustomerCode(code);
    }

    /**
     * 获取所有省份
     */
    public List<String> findAllProvinces() {
        return customerRepository.findAllProvinces();
    }
}
