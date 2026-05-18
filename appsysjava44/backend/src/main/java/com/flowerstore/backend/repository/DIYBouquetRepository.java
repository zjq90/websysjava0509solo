package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.DIYBouquet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DIY花束数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface DIYBouquetRepository extends JpaRepository<DIYBouquet, Long> {

    /**
     * 根据用户ID查询DIY花束
     */
    List<DIYBouquet> findByUserIdOrderByCreateTimeDesc(Long userId);

    /**
     * 根据用户ID和状态查询
     */
    List<DIYBouquet> findByUserIdAndStatusOrderByCreateTimeDesc(Long userId, Integer status);
}
