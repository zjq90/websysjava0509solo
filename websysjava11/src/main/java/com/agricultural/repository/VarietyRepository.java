package com.agricultural.repository;

import com.agricultural.entity.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 品种数据访问层
 */
@Repository
public interface VarietyRepository extends JpaRepository<Variety, Long> {

    /**
     * 根据品种编号查找
     */
    Optional<Variety> findByVarietyCode(String varietyCode);

    /**
     * 根据名称模糊查询
     */
    List<Variety> findByVarietyNameContaining(String name);

    /**
     * 查询所有启用的品种
     */
    List<Variety> findByEnabledTrue();

    /**
     * 根据类别查询
     */
    List<Variety> findByCategory(String category);

    /**
     * 检查品种编号是否存在
     */
    boolean existsByVarietyCode(String varietyCode);

    /**
     * 统计启用的品种数量
     */
    @Query("SELECT COUNT(v) FROM Variety v WHERE v.enabled = true")
    long countEnabled();
}
