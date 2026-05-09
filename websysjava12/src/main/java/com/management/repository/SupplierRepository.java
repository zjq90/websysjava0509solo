package com.management.repository;

import com.management.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 供应商数据访问接口
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    List<Supplier> findBySupplierNameContaining(String keyword);
}
