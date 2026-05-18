package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.ProductCategoryRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品分类关联数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface ProductCategoryRelationRepository extends JpaRepository<ProductCategoryRelation, Long> {

    List<ProductCategoryRelation> findByCategoryId(Long categoryId);

    List<ProductCategoryRelation> findByProductId(Long productId);
}
