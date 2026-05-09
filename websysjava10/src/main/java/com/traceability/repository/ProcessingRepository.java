package com.traceability.repository;

import com.traceability.entity.Processing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 加工数据访问接口
 */
@Repository
public interface ProcessingRepository extends JpaRepository<Processing, Long>, JpaSpecificationExecutor<Processing> {

    List<Processing> findByBatchNo(String batchNo);
}
