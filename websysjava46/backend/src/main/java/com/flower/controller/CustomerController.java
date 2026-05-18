package com.flower.controller;

import com.flower.common.Result;
import com.flower.entity.Customer;
import com.flower.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理控制器
 */
@RestController
@RequestMapping("/api/customers")
@Api(tags = "客户管理接口")
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @ApiOperation("分页查询客户列表")
    public Result<Page<Customer>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(customerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询客户")
    public Result<Customer> getById(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        return customer != null ? Result.success(customer) : Result.error("客户不存在");
    }

    @PostMapping
    @ApiOperation("新增客户")
    public Result<Customer> create(@RequestBody Customer customer) {
        return Result.success(customerService.save(customer));
    }

    @PutMapping("/{id}")
    @ApiOperation("更新客户")
    public Result<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setId(id);
        return Result.success(customerService.save(customer));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除客户")
    public Result<Void> delete(@PathVariable Long id) {
        customerService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}/tags")
    @ApiOperation("更新客户标签")
    public Result<Customer> updateTags(@PathVariable Long id, @RequestParam String tags) {
        Customer customer = customerService.updateTags(id, tags);
        return customer != null ? Result.success(customer) : Result.error("客户不存在");
    }

    @GetMapping("/tags/{tag}")
    @ApiOperation("根据标签查询客户")
    public Result<List<Customer>> getByTags(@PathVariable String tag) {
        return Result.success(customerService.findByTags(tag));
    }

    @PostMapping("/batch-tag")
    @ApiOperation("批量给客户打标签")
    public Result<Integer> batchAddTag(@RequestParam List<Long> customerIds, @RequestParam String tag) {
        int count = customerService.batchAddTag(customerIds, tag);
        return Result.success("成功为 " + count + " 个客户添加标签", count);
    }
}
