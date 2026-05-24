package com.bike.repository;

import com.bike.entity.FaultRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 故障记录数据访问层
 * 
 * @author bike-sharing
 */
@Repository
public interface FaultRecordRepository extends JpaRepository<FaultRecord, Long>, JpaSpecificationExecutor<FaultRecord> {

    Optional<FaultRecord> findByRecordNo(String recordNo);

    List<FaultRecord> findByBikeId(Long bikeId);

    List<FaultRecord> findByStatus(String status);

    List<FaultRecord> findByFaultType(String faultType);

    long countByBikeIdAndStatus(Long bikeId, String status);
}
