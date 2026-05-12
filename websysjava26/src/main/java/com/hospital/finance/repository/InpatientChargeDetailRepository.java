package com.hospital.finance.repository;

import com.hospital.finance.entity.InpatientChargeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 住院收费明细数据访问层
 */
@Repository
public interface InpatientChargeDetailRepository extends JpaRepository<InpatientChargeDetail, Long> {

    /**
     * 根据收费记录ID查询明细
     */
    List<InpatientChargeDetail> findByChargeId(Long chargeId);

    /**
     * 根据收费单号查询明细
     */
    List<InpatientChargeDetail> findByChargeNo(String chargeNo);
}