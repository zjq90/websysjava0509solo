package com.bike.repository;

import com.bike.entity.SparePartLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 备件出入库记录数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface SparePartLogRepository extends JpaRepository<SparePartLog, Long>, JpaSpecificationExecutor<SparePartLog> {

    List<SparePartLog> findByPartId(Long partId);

    List<SparePartLog> findByOperatorId(Long operatorId);

    List<SparePartLog> findByOperationType(String operationType);
}
