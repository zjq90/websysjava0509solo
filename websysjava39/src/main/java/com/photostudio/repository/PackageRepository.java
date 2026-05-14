package com.photostudio.repository;

import com.photostudio.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 套餐Repository
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    
    List<Package> findByTypeAndActive(String type, Boolean active);
    
    List<Package> findByActive(Boolean active);
}
