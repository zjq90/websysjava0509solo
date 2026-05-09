package com.traceability.repository;

import com.traceability.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 销售数据访问接口
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {

    List<Sale> findByBatchNo(String batchNo);

    List<Sale> findByPackageNo(String packageNo);

    Optional<Sale> findBySaleNo(String saleNo);
}
