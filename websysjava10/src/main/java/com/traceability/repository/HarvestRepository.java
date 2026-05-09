package com.traceability.repository;

import com.traceability.entity.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 收获数据访问接口
 */
@Repository
public interface HarvestRepository extends JpaRepository<Harvest, Long>, JpaSpecificationExecutor<Harvest> {

    List<Harvest> findByBatchNo(String batchNo);
}
