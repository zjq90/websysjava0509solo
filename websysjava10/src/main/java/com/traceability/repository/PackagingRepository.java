package com.traceability.repository;

import com.traceability.entity.Packaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 包装数据访问接口
 */
@Repository
public interface PackagingRepository extends JpaRepository<Packaging, Long>, JpaSpecificationExecutor<Packaging> {

    List<Packaging> findByBatchNo(String batchNo);

    Optional<Packaging> findByPackageNo(String packageNo);

    Optional<Packaging> findByQrCode(String qrCode);

    boolean existsByPackageNo(String packageNo);

    boolean existsByQrCode(String qrCode);
}
