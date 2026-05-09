package com.inventory.service;

import com.inventory.entity.Store;
import com.inventory.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 门店Service类
 */
@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    /**
     * 查询所有门店
     */
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    /**
     * 根据ID查询
     */
    public Store findById(Long id) {
        Optional<Store> optional = storeRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 保存门店
     */
    @Transactional(rollbackFor = Exception.class)
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    /**
     * 删除门店
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

    /**
     * 根据状态查询
     */
    public List<Store> findByStatus(Integer status) {
        return storeRepository.findByStatus(status);
    }

    /**
     * 检查名称是否存在
     */
    public boolean existsByName(String name) {
        return storeRepository.existsByName(name);
    }

    /**
     * 检查编码是否存在
     */
    public boolean existsByCode(String code) {
        return storeRepository.existsByCode(code);
    }
}
