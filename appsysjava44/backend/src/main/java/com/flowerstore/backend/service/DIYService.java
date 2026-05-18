package com.flowerstore.backend.service;

import com.flowerstore.backend.dto.Result;
import com.flowerstore.backend.entity.DIYBouquet;
import com.flowerstore.backend.repository.DIYBouquetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * DIY花束服务类
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Slf4j
@Service
public class DIYService {

    @Autowired
    private DIYBouquetRepository diyBouquetRepository;

    /**
     * 保存DIY花束
     */
    @Transactional
    public Result<DIYBouquet> saveDIYBouquet(Long userId, DIYBouquet bouquet) {
        bouquet.setUserId(userId);
        bouquet.setStatus(0);
        bouquet = diyBouquetRepository.save(bouquet);
        return Result.success("花束保存成功", bouquet);
    }

    /**
     * 获取用户DIY花束列表
     */
    public Result<List<DIYBouquet>> getUserDIYBouquets(Long userId, Integer status) {
        List<DIYBouquet> bouquets;
        if (status != null) {
            bouquets = diyBouquetRepository.findByUserIdAndStatusOrderByCreateTimeDesc(userId, status);
        } else {
            bouquets = diyBouquetRepository.findByUserIdOrderByCreateTimeDesc(userId);
        }
        return Result.success(bouquets);
    }

    /**
     * 获取DIY花束详情
     */
    public Result<DIYBouquet> getDIYBouquetDetail(Long userId, Long bouquetId) {
        Optional<DIYBouquet> bouquetOpt = diyBouquetRepository.findById(bouquetId);
        if (bouquetOpt.isEmpty()) {
            return Result.error("花束不存在");
        }
        DIYBouquet bouquet = bouquetOpt.get();
        
        if (!bouquet.getUserId().equals(userId)) {
            return Result.error("无权访问该花束");
        }
        
        return Result.success(bouquet);
    }

    /**
     * 更新DIY花束
     */
    @Transactional
    public Result<DIYBouquet> updateDIYBouquet(Long userId, Long bouquetId, DIYBouquet bouquet) {
        Optional<DIYBouquet> existOpt = diyBouquetRepository.findById(bouquetId);
        if (existOpt.isEmpty()) {
            return Result.error("花束不存在");
        }
        DIYBouquet existBouquet = existOpt.get();
        
        if (!existBouquet.getUserId().equals(userId)) {
            return Result.error("无权修改该花束");
        }
        
        if (bouquet.getMainFlowers() != null) existBouquet.setMainFlowers(bouquet.getMainFlowers());
        if (bouquet.getSideFlowers() != null) existBouquet.setSideFlowers(bouquet.getSideFlowers());
        if (bouquet.getPackagingColor() != null) existBouquet.setPackagingColor(bouquet.getPackagingColor());
        if (bouquet.getPackagingStyle() != null) existBouquet.setPackagingStyle(bouquet.getPackagingStyle());
        if (bouquet.getGreetingCard() != null) existBouquet.setGreetingCard(bouquet.getGreetingCard());
        if (bouquet.getRemarks() != null) existBouquet.setRemarks(bouquet.getRemarks());
        if (bouquet.getEstimatedPrice() != null) existBouquet.setEstimatedPrice(bouquet.getEstimatedPrice());
        
        diyBouquetRepository.save(existBouquet);
        return Result.success("花束更新成功", existBouquet);
    }

    /**
     * 删除DIY花束
     */
    @Transactional
    public Result<String> deleteDIYBouquet(Long userId, Long bouquetId) {
        Optional<DIYBouquet> bouquetOpt = diyBouquetRepository.findById(bouquetId);
        if (bouquetOpt.isEmpty()) {
            return Result.error("花束不存在");
        }
        DIYBouquet bouquet = bouquetOpt.get();
        
        if (!bouquet.getUserId().equals(userId)) {
            return Result.error("无权删除该花束");
        }
        
        diyBouquetRepository.deleteById(bouquetId);
        return Result.success("花束删除成功");
    }
}
