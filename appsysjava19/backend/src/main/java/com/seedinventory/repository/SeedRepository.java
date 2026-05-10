package com.seedinventory.repository;

import com.seedinventory.entity.Seed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * 种子数据访问层
 * 
 * @author Seed Inventory Team
 * @version 1.0.0
 */
@Repository
public interface SeedRepository extends JpaRepository<Seed, Long> {
    
    /**
     * 根据种子编号查询
     */
    Optional<Seed> findBySeedCode(String seedCode);
    
    /**
     * 根据状态查询种子列表
     */
    List<Seed> findByStatus(String status);
    
    /**
     * 根据类别查询
     */
    List<Seed> findByCategory(String category);
    
    /**
     * 判断种子编号是否存在
     */
    boolean existsBySeedCode(String seedCode);
}
