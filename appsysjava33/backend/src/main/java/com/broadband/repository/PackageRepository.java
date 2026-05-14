package com.broadband.repository;

import com.broadband.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 套餐数据访问层
 * 
 * @author broadband
 * @version 1.0.0
 */
@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    List<Package> findByStatusOrderBySortAsc(Integer status);

    List<Package> findByTypeAndStatusOrderBySortAsc(Integer type, Integer status);

    List<Package> findByParentIdAndStatusOrderBySortAsc(Long parentId, Integer status);
}
