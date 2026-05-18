package com.heritage.repository;

import com.heritage.entity.Heritage;
import com.heritage.enums.AuditStatus;
import com.heritage.enums.RiskLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeritageRepository extends JpaRepository<Heritage, Long>, JpaSpecificationExecutor<Heritage> {

    List<Heritage> findByAuditStatus(AuditStatus status);

    List<Heritage> findByAuditStatusOrderByRiskLevelDesc(AuditStatus status);

    List<Heritage> findByRiskLevel(RiskLevel riskLevel);

    List<Heritage> findByDynasty(String dynasty);

    List<Heritage> findByMaterial(String material);

    List<Heritage> findByUsageType(String usageType);

    List<Heritage> findByProvince(String province);

    List<Heritage> findByCity(String city);
}
