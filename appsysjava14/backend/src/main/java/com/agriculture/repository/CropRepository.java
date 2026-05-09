package com.agriculture.repository;

import com.agriculture.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 作物品种数据访问层
 * 提供作物品种表的基本CRUD操作
 * 
 * @author Agriculture System
 * @version 1.0.0
 */
@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {

    /**
     * 根据批次编号查找
     * 
     * @param batchCode 批次编号
     * @return 作物对象
     */
    Optional<Crop> findByBatchCode(String batchCode);

    /**
     * 根据作物类型查找
     * 
     * @param cropType 作物类型
     * @return 作物列表
     */
    List<Crop> findByCropTypeOrderByCreatedAtDesc(String cropType);

    /**
     * 根据状态查找
     * 
     * @param status 状态
     * @return 作物列表
     */
    List<Crop> findByStatusOrderByCreatedAtDesc(String status);

    /**
     * 检查批次编号是否存在
     * 
     * @param batchCode 批次编号
     * @return 是否存在
     */
    boolean existsByBatchCode(String batchCode);
}
