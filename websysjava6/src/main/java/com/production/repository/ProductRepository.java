package com.production.repository;

import com.production.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 产品数据访问层
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 根据产品编号查询
     */
    Optional<Product> findByProductCode(String productCode);

    /**
     * 根据产品名称模糊查询
     */
    List<Product> findByProductNameContaining(String productName);

    /**
     * 根据状态查询
     */
    List<Product> findByStatus(String status);

    /**
     * 根据产品类型查询
     */
    List<Product> findByProductType(String productType);

    /**
     * 检查产品编号是否存在
     */
    boolean existsByProductCode(String productCode);

    /**
     * 查询所有激活的产品
     */
    @Query("SELECT p FROM Product p WHERE p.status = 'ACTIVE' ORDER BY p.createTime DESC")
    List<Product> findAllActive();
}