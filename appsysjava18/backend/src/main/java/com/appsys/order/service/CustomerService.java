package com.appsys.order.service;

import com.appsys.common.exception.BusinessException;
import com.appsys.common.util.AESUtil;
import com.appsys.order.dto.CustomerDTO;
import com.appsys.order.entity.Customer;
import com.appsys.order.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户管理服务类
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AESUtil aesUtil;

    /**
     * 查询所有客户列表
     */
    public List<Customer> list() {
        List<Customer> customers = customerRepository.findByDeletedFalseOrderByCreatedTimeDesc();
        // 解密手机号（脱敏显示）
        customers.forEach(c -> {
            if (c.getPhone() != null) {
                c.setPhone(aesUtil.decryptPhone(c.getPhone(), true));
            }
        });
        return customers;
    }

    /**
     * 根据ID查询客户详情
     */
    public Customer getById(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("客户不存在"));
        // 解密手机号（脱敏显示）
        if (customer.getPhone() != null) {
            customer.setPhone(aesUtil.decryptPhone(customer.getPhone(), true));
        }
        return customer;
    }

    /**
     * 新增客户
     */
    @Transactional
    public Customer create(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setCustomerName(dto.getCustomerName());
        // 加密手机号存储
        if (dto.getPhone() != null) {
            customer.setPhone(aesUtil.encrypt(dto.getPhone()));
        }
        customer.setAddress(dto.getAddress());
        customer.setEmail(dto.getEmail());
        customer.setCustomerType(dto.getCustomerType());
        customer.setRemark(dto.getRemark());
        return customerRepository.save(customer);
    }

    /**
     * 更新客户信息
     */
    @Transactional
    public Customer update(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("客户不存在"));
        
        customer.setCustomerName(dto.getCustomerName());
        // 更新手机号时重新加密
        if (dto.getPhone() != null) {
            customer.setPhone(aesUtil.encrypt(dto.getPhone()));
        }
        customer.setAddress(dto.getAddress());
        customer.setEmail(dto.getEmail());
        customer.setCustomerType(dto.getCustomerType());
        customer.setRemark(dto.getRemark());
        return customerRepository.save(customer);
    }

    /**
     * 删除客户（逻辑删除）
     */
    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new BusinessException("客户不存在"));
        customer.setDeleted(true);
        customerRepository.save(customer);
    }
}
