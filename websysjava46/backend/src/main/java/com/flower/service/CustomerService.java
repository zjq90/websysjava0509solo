package com.flower.service;

import com.flower.entity.Customer;
import com.flower.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 客户服务类
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 分页查询客户
     */
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    /**
     * 根据ID查询客户
     */
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    /**
     * 保存客户
     */
    @Transactional
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    /**
     * 删除客户
     */
    @Transactional
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    /**
     * 更新客户标签
     */
    @Transactional
    public Customer updateTags(Long id, String tags) {
        Customer customer = findById(id);
        if (customer != null) {
            customer.setTags(tags);
            return customerRepository.save(customer);
        }
        return null;
    }

    /**
     * 根据标签查询客户
     */
    public List<Customer> findByTags(String tag) {
        return customerRepository.findByTagsContaining(tag);
    }

    /**
     * 批量给客户打标签
     */
    @Transactional
    public int batchAddTag(List<Long> customerIds, String tag) {
        int count = 0;
        for (Long id : customerIds) {
            Customer customer = findById(id);
            if (customer != null) {
                String currentTags = customer.getTags();
                if (currentTags == null || currentTags.isEmpty()) {
                    customer.setTags(tag);
                } else if (!currentTags.contains(tag)) {
                    customer.setTags(currentTags + "," + tag);
                }
                customerRepository.save(customer);
                count++;
            }
        }
        return count;
    }
}
