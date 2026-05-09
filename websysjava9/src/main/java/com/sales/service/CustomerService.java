package com.sales.service;

import com.sales.entity.Customer;
import com.sales.entity.CustomerLevel;
import com.sales.entity.CustomerType;
import com.sales.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 客户服务层
 * 处理客户档案管理的业务逻辑，支持分级与标签化管理
 */
@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 获取所有客户
     */
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    /**
     * 获取所有启用的客户
     */
    public List<Customer> findAllActive() {
        return customerRepository.findByActiveTrue();
    }

    /**
     * 根据ID获取客户
     */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    /**
     * 根据编码获取客户
     */
    public Optional<Customer> findByCode(String code) {
        return customerRepository.findByCode(code);
    }

    /**
     * 保存客户
     */
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * 更新客户
     */
    public Customer update(Long id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setName(customerDetails.getName());
            customer.setType(customerDetails.getType());
            customer.setLevel(customerDetails.getLevel());
            customer.setContactPerson(customerDetails.getContactPerson());
            customer.setPhone(customerDetails.getPhone());
            customer.setEmail(customerDetails.getEmail());
            customer.setProvince(customerDetails.getProvince());
            customer.setCity(customerDetails.getCity());
            customer.setAddress(customerDetails.getAddress());
            customer.setTags(customerDetails.getTags());
            customer.setRemark(customerDetails.getRemark());
            customer.setActive(customerDetails.getActive());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("客户不存在: " + id));
    }

    /**
     * 删除客户
     */
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * 按名称搜索
     */
    public List<Customer> searchByName(String name) {
        return customerRepository.findByNameContaining(name);
    }

    /**
     * 按客户类型查找
     */
    public List<Customer> findByType(CustomerType type) {
        return customerRepository.findByType(type);
    }

    /**
     * 按客户等级查找
     */
    public List<Customer> findByLevel(CustomerLevel level) {
        return customerRepository.findByLevel(level);
    }

    /**
     * 按省份查找
     */
    public List<Customer> findByProvince(String province) {
        return customerRepository.findByProvince(province);
    }

    /**
     * 按标签查找
     */
    public List<Customer> findByTag(String tag) {
        return customerRepository.findByTagsContaining(tag);
    }

    /**
     * 分页查询所有客户
     */
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAllByOrderByCreateTimeDesc(pageable);
    }

    /**
     * 分页查询所有启用的客户
     */
    public Page<Customer> findAllActive(Pageable pageable) {
        return customerRepository.findByActiveTrueOrderByCreateTimeDesc(pageable);
    }

    /**
     * 按类型分页查询
     */
    public Page<Customer> findByType(CustomerType type, Pageable pageable) {
        return customerRepository.findByTypeOrderByCreateTimeDesc(type, pageable);
    }

    /**
     * 按等级分页查询
     */
    public Page<Customer> findByLevel(CustomerLevel level, Pageable pageable) {
        return customerRepository.findByLevelOrderByCreateTimeDesc(level, pageable);
    }

    /**
     * 按名称模糊分页查询
     */
    public Page<Customer> searchByName(String name, Pageable pageable) {
        return customerRepository.findByNameContainingOrderByCreateTimeDesc(name, pageable);
    }
}
