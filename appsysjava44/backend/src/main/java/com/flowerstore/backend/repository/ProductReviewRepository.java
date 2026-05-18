package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品评价数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    List<ProductReview> findByProductIdAndStatusOrderByCreateTimeDesc(Long productId, Integer status);

    List<ProductReview> findByUserIdOrderByCreateTimeDesc(Long userId);
}
