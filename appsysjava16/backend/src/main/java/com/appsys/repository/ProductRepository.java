package com.appsys.repository;

import com.appsys.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 产品数据访问接口
 * 
 * @author appsys-team
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 根据批次编号查询产品
     * @param batchNumber 批次编号
     * @return 产品
     */
    Optional<Product> findByBatchNumber(String batchNumber);

    /**
     * 检查批次编号是否已存在
     * @param batchNumber 批次编号
     * @return 是否存在
     */
    boolean existsByBatchNumber(String batchNumber);

    /**
     * 根据产品名称模糊查询
     * @param name 产品名称关键字
     * @return 产品列表
     */
    List<Product> findByNameContaining(String name);

    /**
     * 根据分类查询产品
     * @param category 产品分类
     * @return 产品列表
     */
    List<Product> findByCategory(String category);

    /**
     * 查询库存大于指定数量的产品
     * @param quantity 库存数量
     * @return 产品列表
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > :quantity ORDER BY p.createdAt DESC")
    List<Product> findAvailableProducts(@Param("quantity") Integer quantity);

    /**
     * 分页查询产品并按创建时间倒序
     * @return 分页数据
     */
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findAllOrderByCreatedAtDesc();
}
