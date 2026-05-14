package com.photostudio.repository;

import com.photostudio.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 套餐数据访问接口
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Long>, JpaSpecificationExecutor<Package> {

    /**
     * 根据类型查询套餐
     */
    List<Package> findByType(String type);

    /**
     * 查询所有上架套餐
     */
    List<Package> findByStatusOrderBySortOrderAsc(Integer status);

    /**
     * 根据名称模糊查询
     */
    List<Package> findByNameContaining(String name);
}
