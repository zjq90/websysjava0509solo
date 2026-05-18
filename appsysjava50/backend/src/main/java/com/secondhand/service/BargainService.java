package com.secondhand.service;

import com.secondhand.entity.BargainActivity;
import com.secondhand.entity.BargainHelp;
import com.secondhand.entity.BargainRecord;
import com.secondhand.entity.Product;
import com.secondhand.repository.BargainActivityRepository;
import com.secondhand.repository.BargainHelpRepository;
import com.secondhand.repository.BargainRecordRepository;
import com.secondhand.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

/**
 * 砍价服务类
 *
 * @author secondhand
 * @version 1.0.0
 */
@Service
public class BargainService {

    @Autowired
    private BargainActivityRepository activityRepository;

    @Autowired
    private BargainRecordRepository recordRepository;

    @Autowired
    private BargainHelpRepository helpRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * 创建砍价活动
     */
    public BargainActivity createActivity(BargainActivity activity) {
        Product product = productRepository.findByIdAndIsDeletedFalse(activity.getProductId()).orElse(null);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        return activityRepository.save(activity);
    }

    /**
     * 获取所有有效砍价活动
     */
    public List<BargainActivity> getActiveActivities() {
        List<BargainActivity> activities = activityRepository.findByStatusOrderByCreateTimeDesc("ACTIVE");
        for (BargainActivity activity : activities) {
            Product product = productRepository.findByIdAndIsDeletedFalse(activity.getProductId()).orElse(null);
            activity.setProduct(product);
        }
        return activities;
    }

    /**
     * 发起砍价
     */
    @Transactional
    public BargainRecord startBargain(Long userId, Long activityId) {
        BargainActivity activity = activityRepository.findById(activityId).orElse(null);
        if (activity == null || !"ACTIVE".equals(activity.getStatus())) {
            throw new RuntimeException("砍价活动不存在或已结束");
        }

        // 检查是否已经发起过
        if (recordRepository.existsByActivityIdAndInitiatorId(activityId, userId)) {
            throw new RuntimeException("您已经发起过此砍价");
        }

        BargainRecord record = new BargainRecord();
        record.setActivityId(activityId);
        record.setInitiatorId(userId);
        record.setCurrentPrice(activity.getOriginalPrice());
        record.setHelpCount(0);
        return recordRepository.save(record);
    }

    /**
     * 帮砍一刀
     */
    @Transactional
    public BargainHelp helpBargain(Long helperId, Long recordId) {
        BargainRecord record = recordRepository.findById(recordId).orElse(null);
        if (record == null) {
            throw new RuntimeException("砍价记录不存在");
        }

        if ("SUCCESS".equals(record.getStatus())) {
            throw new RuntimeException("砍价已完成，不能再砍");
        }

        BargainActivity activity = activityRepository.findById(record.getActivityId()).orElse(null);
        if (activity == null) {
            throw new RuntimeException("砍价活动不存在");
        }

        // 检查是否已经帮砍过
        if (helpRepository.existsByRecordIdAndHelperId(recordId, helperId)) {
            throw new RuntimeException("您已经帮砍过了");
        }

        // 不能自己砍自己
        if (record.getInitiatorId().equals(helperId)) {
            throw new RuntimeException("不能自己砍自己");
        }

        // 检查是否达到最大帮砍次数
        if (record.getHelpCount() >= activity.getMaxHelpCount()) {
            throw new RuntimeException("已达到最大帮砍次数");
        }

        // 计算砍价金额（随机金额，越接近底价
        BigDecimal remainAmount = record.getCurrentPrice().subtract(activity.getMinPrice());
        if (remainAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("已砍到最低价");
        }

        // 剩余帮砍次数
        int remainHelp = activity.getMaxHelpCount() - record.getHelpCount();
        
        // 每次平均金额
        BigDecimal average = remainAmount.divide(new BigDecimal(remainHelp), RoundingMode.DOWN);
        
        // 随机浮动（0.5-1.5倍平均
        Random random = new Random();
        double factor = 0.5 + random.nextDouble();
        BigDecimal cutAmount = average.multiply(new BigDecimal(factor)).setScale(2, RoundingMode.DOWN);
        
        // 确保不超过剩余
        if (cutAmount.compareTo(remainAmount) > 0) {
            cutAmount = remainAmount;
        }
        
        // 确保砍完后不低于底价
        BigDecimal newPrice = record.getCurrentPrice().subtract(cutAmount);
        if (newPrice.compareTo(activity.getMinPrice()) < 0) {
            cutAmount = record.getCurrentPrice().subtract(activity.getMinPrice());
            newPrice = activity.getMinPrice();
        }

        // 创建帮砍记录
        BargainHelp help = new BargainHelp();
        help.setRecordId(recordId);
        help.setHelperId(helperId);
        help.setAmount(cutAmount);
        help = helpRepository.save(help);

        // 更新砍价记录
        record.setCurrentPrice(newPrice);
        record.setHelpCount(record.getHelpCount() + 1);
        
        // 检查是否砍到底价
        if (newPrice.compareTo(activity.getMinPrice()) <= 0) {
            record.setStatus("SUCCESS");
        }
        
        recordRepository.save(record);

        return help;
    }

    /**
     * 获取我的砍价记录
     */
    public List<BargainRecord> getMyBargainRecords(Long userId) {
        return recordRepository.findByInitiatorIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 获取砍价详情
     */
    public List<BargainHelp> getBargainHelpRecords(Long recordId) {
        return helpRepository.findByRecordIdOrderByCreateTimeDesc(recordId);
    }

    /**
     * 获取砍价记录详情
     */
    public BargainRecord getBargainRecord(Long recordId) {
        return recordRepository.findById(recordId).orElse(null);
    }

}
