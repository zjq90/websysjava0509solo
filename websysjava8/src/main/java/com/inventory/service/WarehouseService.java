package com.inventory.service;

import com.inventory.entity.Warehouse;
import com.inventory.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 仓库服务层
 * 功能：处理仓库的增删改查业务逻辑
 */
@Service
@Transactional
public class WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouseRepository.findById(id);
    }

    public Optional<Warehouse> findByCode(String code) {
        return warehouseRepository.findByWarehouseCode(code);
    }

    public Warehouse save(Warehouse warehouse) {
        return warehouseRepository.save(warehouse);
    }

    public void deleteById(Long id) {
        warehouseRepository.deleteById(id);
    }
}
