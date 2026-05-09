package com.management.service;

import com.management.entity.Supplier;
import com.management.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 供应商服务类
 * 提供供应商的增删改查功能
 */
@Service
@Transactional
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier save(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public void deleteById(Long id) {
        supplierRepository.deleteById(id);
    }

    public List<Supplier> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        return supplierRepository.findBySupplierNameContaining(keyword);
    }
}
