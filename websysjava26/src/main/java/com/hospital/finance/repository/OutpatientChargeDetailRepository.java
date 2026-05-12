package com.hospital.finance.repository;

import com.hospital.finance.entity.OutpatientChargeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 门诊收费明细数据访问层
 */
@Repository
public interface OutpatientChargeDetailRepository extends JpaRepository<OutpatientChargeDetail, Long> {

    /**
     * 根据收费记录ID查询明细
     */
    List<OutpatientChargeDetail> findByChargeId(Long chargeId);

    /**
     * 根据收费单号查询明细
     */
    List<OutpatientChargeDetail> findByChargeNo(String chargeNo);
}