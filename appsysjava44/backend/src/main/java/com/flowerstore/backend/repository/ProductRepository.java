package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    /**
     * 根据状态查询产品列表
     */
    List<Product> findByStatus(Integer status);

    /**
     * 查询热门产品
     */
    List<Product> findByIsHotAndStatus(Integer isHot, Integer status);

    /**
     * 查询新品
     */
    List<Product> findByIsNewAndStatus(Integer isNew, Integer status);

    /**
     * 查询推荐产品
     */
    List<Product> findByIsRecommendAndStatus(Integer isRecommend, Integer status);

    /**
     * 根据名称模糊搜索
     */
    @Query("SELECT p FROM Product p WHERE p.status = 1 AND (p.name LIKE %?1% OR p.subtitle LIKE %?1% OR p.tags LIKE %?1%)")
    List<Product> searchByKeyword(String keyword);

    /**
     * 根据价格区间查询
     */
    List<Product> findByPriceBetweenAndStatus(Long minPrice, Long maxPrice, Integer status);
}
