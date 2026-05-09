package com.traceability.repository;

import com.traceability.entity.FieldPlanting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 田间种植数据访问接口
 */
@Repository
public interface FieldPlantingRepository extends JpaRepository<FieldPlanting, Long>, JpaSpecificationExecutor<FieldPlanting> {

    List<FieldPlanting> findByBatchNo(String batchNo);
}
