package com.traceability.repository;

import com.traceability.entity.QrTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 二维码追踪数据访问接口
 */
@Repository
public interface QrTraceRepository extends JpaRepository<QrTrace, Long>, JpaSpecificationExecutor<QrTrace> {

    List<QrTrace> findByQrCode(String qrCode);

    List<QrTrace> findByPackageNo(String packageNo);

    List<QrTrace> findByBatchNo(String batchNo);
}
