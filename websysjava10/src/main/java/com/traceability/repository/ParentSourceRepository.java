package com.traceability.repository;

import com.traceability.entity.ParentSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 亲本来源数据访问接口
 */
@Repository
public interface ParentSourceRepository extends JpaRepository<ParentSource, Long>, JpaSpecificationExecutor<ParentSource> {

    List<ParentSource> findByBatchNo(String batchNo);

    Optional<ParentSource> findByBatchNoAndDeletedFalse(String batchNo);
}
