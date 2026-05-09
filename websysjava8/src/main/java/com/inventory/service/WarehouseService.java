package com.inventory.service;

import com.inventory.entity.Warehouse;
import com.inventory.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 仓库Service类
 */
@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    /**
     * 查询所有仓库
     */
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public Warehouse findById(Long id) {
        Optional<Warehouse> optional = warehouseRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 保存仓库
     */
    @Transactional(rollbackFor = Exception.class)
    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    /**
     * 删除仓库
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        warehouseRepository.deleteById(id);
    }

    /**
     * 根据状态查询
     */
    public List<Warehouse> findByStatus(Integer status) {
        return warehouseRepository.findByStatus(status);
    }

    /**
     * 检查名称是否存在
     */
    public boolean existsByName(String name) {
        return warehouseRepository.existsByName(name);
    }

    /**
     * 检查编码是否存在
     */
    public boolean existsByCode(String code) {
        return warehouseRepository.existsByCode(code);
    }
}
