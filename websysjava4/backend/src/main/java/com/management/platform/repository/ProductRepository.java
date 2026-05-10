package com.management.platform.repository;

import com.management.platform.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品数据访问层
 * 提供商品的增删改查和统计查询功能
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 查询热销商品（按销售数量降序排列）
     * @param status 商品状态
     * @return 热销商品列表
     */
    List<Product> findByStatusOrderBySalesQuantityDesc(String status);

    /**
     * 查询滞销商品（销售数量为0或库存较多的商品）
     * @param status 商品状态
     * @return 滞销商品列表
     */
    @Query("SELECT p FROM Product p WHERE p.status = ?1 AND p.salesQuantity = 0 ORDER BY p.stockQuantity DESC")
    List<Product> findUnsoldProducts(String status);

    /**
     * 按分类统计商品数量
     * @param status 商品状态
     * @return 分类和数量列表
     */
    @Query("SELECT p.category, COUNT(p) FROM Product p WHERE p.status = ?1 GROUP BY p.category")
    List<Object[]> countByCategory(String status);
}
