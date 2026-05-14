package com.photostudio.service;

import com.photostudio.common.Result;
import com.photostudio.entity.Customer;
import com.photostudio.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 客户服务类
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * 查询所有客户
     */
    public Result<List<Customer>> findAll() {
        List<Customer> list = customerRepository.findAll();
        return Result.success(list);
    }

    /**
     * 分页查询客户
     */
    public Result<Page<Customer>> findPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<Customer> result = customerRepository.findAll(pageable);
        return Result.success(result);
    }

    /**
     * 根据ID查询客户
     */
    public Result<Customer> findById(Long id) {
        Optional<Customer> optional = customerRepository.findById(id);
        return optional.map(Result::success).orElse(Result.error("客户不存在"));
    }

    /**
     * 根据手机号查询客户
     */
    public Result<Customer> findByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone);
        return Result.success(customer);
    }

    /**
     * 新增客户
     */
    public Result<Customer> add(Customer customer) {
        Customer exist = customerRepository.findByPhone(customer.getPhone());
        if (exist != null) {
            return Result.error("该手机号已存在客户");
        }
        Customer saved = customerRepository.save(customer);
        return Result.success("客户创建成功", saved);
    }

    /**
     * 更新客户
     */
    public Result<Customer> update(Customer customer) {
        if (customer.getId() == null) {
            return Result.error("客户ID不能为空");
        }
        Optional<Customer> optional = customerRepository.findById(customer.getId());
        if (!optional.isPresent()) {
            return Result.error("客户不存在");
        }
        Customer saved = customerRepository.save(customer);
        return Result.success("客户更新成功", saved);
    }

    /**
     * 删除客户
     */
    public Result<Void> delete(Long id) {
        if (!customerRepository.existsById(id)) {
            return Result.error("客户不存在");
        }
        customerRepository.deleteById(id);
        return Result.success("客户删除成功", null);
    }

    /**
     * 模糊查询客户
     */
    public Result<List<Customer>> search(String keyword) {
        List<Customer> list = customerRepository.findByNameContaining(keyword);
        return Result.success(list);
    }

    /**
     * 根据来源查询客户
     */
    public Result<List<Customer>> findBySource(String source) {
        List<Customer> list = customerRepository.findBySource(source);
        return Result.success(list);
    }
}
