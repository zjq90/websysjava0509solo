package com.inventory.service;

import com.inventory.entity.Store;
import com.inventory.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 门店服务层
 * 功能：处理门店的增删改查业务逻辑
 */
@Service
@Transactional
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    public Optional<Store> findById(Long id) {
        return storeRepository.findById(id);
    }

    public Optional<Store> findByCode(String code) {
        return storeRepository.findByStoreCode(code);
    }

    public Store save(Store store) {
        return storeRepository.save(store);
    }

    public void deleteById(Long id) {
        storeRepository.deleteById(id);
    }
}
