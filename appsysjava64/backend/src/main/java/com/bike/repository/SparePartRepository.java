package com.bike.repository;

import com.bike.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 备件数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface SparePartRepository extends JpaRepository<SparePart, Long>, JpaSpecificationExecutor<SparePart> {

    Optional<SparePart> findByPartCode(String partCode);

    List<SparePart> findByCategory(String category);

    List<SparePart> findByStockQuantityLessThanEqual(Integer quantity);

    List<SparePart> findByStatus(String status);
}
