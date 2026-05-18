package com.flower.service;

import com.flower.entity.MarketingCampaign;
import com.flower.repository.MarketingCampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 营销活动服务类
 */
@Service
public class MarketingService {

    @Autowired
    private MarketingCampaignRepository marketingCampaignRepository;

    /**
     * 分页查询营销活动
     */
    public Page<MarketingCampaign> findAll(Pageable pageable) {
        return marketingCampaignRepository.findAll(pageable);
    }

    /**
     * 根据ID查询营销活动
     */
    public MarketingCampaign findById(Long id) {
        return marketingCampaignRepository.findById(id).orElse(null);
    }

    /**
     * 保存营销活动
     */
    @Transactional
    public MarketingCampaign save(MarketingCampaign campaign) {
        return marketingCampaignRepository.save(campaign);
    }

    /**
     * 删除营销活动
     */
    @Transactional
    public void delete(Long id) {
        marketingCampaignRepository.deleteById(id);
    }

    /**
     * 更新活动状态
     */
    @Transactional
    public MarketingCampaign updateStatus(Long id, Integer status) {
        MarketingCampaign campaign = findById(id);
        if (campaign != null) {
            campaign.setStatus(status);
            return marketingCampaignRepository.save(campaign);
        }
        return null;
    }

    /**
     * 获取进行中的活动
     */
    public List<MarketingCampaign> getActiveCampaigns() {
        return marketingCampaignRepository.findByStatus(1);
    }
}
