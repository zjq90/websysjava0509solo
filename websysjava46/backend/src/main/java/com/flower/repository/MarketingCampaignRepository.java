package com.flower.repository;

import com.flower.entity.MarketingCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 营销活动数据访问层
 */
@Repository
public interface MarketingCampaignRepository extends JpaRepository<MarketingCampaign, Long>, JpaSpecificationExecutor<MarketingCampaign> {

    /**
     * 根据状态查询活动
     */
    List<MarketingCampaign> findByStatus(Integer status);

    /**
     * 根据类型查询活动
     */
    List<MarketingCampaign> findByType(Integer type);
}
