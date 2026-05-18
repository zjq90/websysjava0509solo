package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品分类数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    List<ProductCategory> findByStatusOrderBySortOrderAsc(Integer status);

    List<ProductCategory> findByCategoryTypeAndStatusOrderBySortOrderAsc(String categoryType, Integer status);
}
