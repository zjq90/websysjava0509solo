package com.culturalrelic.repository;

import com.culturalrelic.entity.DigitalTwin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数字分身数据访问层
 */
@Repository
public interface DigitalTwinRepository extends JpaRepository<DigitalTwin, Long>, JpaSpecificationExecutor<DigitalTwin> {

    /**
     * 根据文物ID查询
     */
    List<DigitalTwin> findByRelicId(Long relicId);

    /**
     * 根据状态查询
     */
    List<DigitalTwin> findByStatus(Integer status);

    /**
     * 查询支持虚拟修复的
     */
    List<DigitalTwin> findBySupportRestorationDemo(Integer support);

    /**
     * 逻辑删除
     */
    @Modifying
    @Query("UPDATE DigitalTwin t SET t.deleted = 1 WHERE t.id = :id")
    int logicDelete(@Param("id") Long id);
}
