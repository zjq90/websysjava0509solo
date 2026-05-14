package com.photostudio.controller;

import com.photostudio.common.Result;
import com.photostudio.entity.Store;
import com.photostudio.exception.ResourceNotFoundException;
import com.photostudio.repository.StoreRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 门店控制器
 * 提供门店的增删改查API接口
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
@Tag(name = "门店管理", description = "门店CRUD API")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    /**
     * 获取所有门店
     */
    @GetMapping
    @Operation(summary = "获取所有门店", description = "获取所有门店列表")
    public Result<List<Store>> getAllStores() {
        return Result.success(storeRepository.findAll());
    }

    /**
     * 根据ID获取门店
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取门店详情", description = "根据ID获取门店详情")
    public Result<Store> getStoreById(@PathVariable Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("门店不存在: " + id));
        return Result.success(store);
    }

    /**
     * 创建门店
     */
    @PostMapping
    @Operation(summary = "创建门店", description = "创建新的门店")
    public Result<Store> createStore(@RequestBody Store store) {
        return Result.success(storeRepository.save(store));
    }

    /**
     * 更新门店
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新门店", description = "根据ID更新门店信息")
    public Result<Store> updateStore(@PathVariable Long id, @RequestBody Store storeDetails) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("门店不存在: " + id));
        
        store.setName(storeDetails.getName());
        store.setAddress(storeDetails.getAddress());
        store.setPhone(storeDetails.getPhone());
        store.setManager(storeDetails.getManager());
        store.setMonthlyTarget(storeDetails.getMonthlyTarget());
        store.setActive(storeDetails.getActive());
        
        return Result.success(storeRepository.save(store));
    }

    /**
     * 删除门店
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除门店", description = "根据ID删除门店")
    public Result<Void> deleteStore(@PathVariable Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("门店不存在: " + id));
        
        storeRepository.delete(store);
        return Result.success();
    }
}
