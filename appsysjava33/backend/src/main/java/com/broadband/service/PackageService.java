package com.broadband.service;

import com.broadband.entity.Package;
import com.broadband.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 套餐服务
 * 处理套餐管理相关功能
 * 
 * @author broadband
 * @version 1.0.0
 */
@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    public List<Package> getAllPackages() {
        return packageRepository.findByStatusOrderBySortAsc(1);
    }

    public List<Package> getPackagesByType(Integer type) {
        return packageRepository.findByTypeAndStatusOrderBySortAsc(type, 1);
    }

    public Package getPackageById(Long id) {
        return packageRepository.findById(id).orElseThrow(() -> new RuntimeException("套餐不存在"));
    }

    public List<Package> getExtraPackages(Long parentId) {
        return packageRepository.findByParentIdAndStatusOrderBySortAsc(parentId, 1);
    }
}
