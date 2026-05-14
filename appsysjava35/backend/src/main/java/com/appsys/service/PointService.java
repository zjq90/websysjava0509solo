package com.appsys.service;

import com.appsys.entity.*;
import com.appsys.repository.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PointService {

    private final PointRecordRepository pointRecordRepository;
    private final PointGoodsRepository pointGoodsRepository;
    private final UserRepository userRepository;
    private final CheckInRecordRepository checkInRecordRepository;
    private final MemberLevelRepository memberLevelRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public PointService(PointRecordRepository pointRecordRepository,
                        PointGoodsRepository pointGoodsRepository,
                        UserRepository userRepository,
                        CheckInRecordRepository checkInRecordRepository,
                        MemberLevelRepository memberLevelRepository,
                        RedisTemplate<String, Object> redisTemplate) {
        this.pointRecordRepository = pointRecordRepository;
        this.pointGoodsRepository = pointGoodsRepository;
        this.userRepository = userRepository;
        this.checkInRecordRepository = checkInRecordRepository;
        this.memberLevelRepository = memberLevelRepository;
        this.redisTemplate = redisTemplate;
    }

    public List<PointGoods> getAllPointGoods() {
        return pointGoodsRepository.findByStatusOrderBySortAsc(1);
    }

    public List<PointGoods> getPointGoodsByType(Integer type) {
        return pointGoodsRepository.findByTypeAndStatusOrderBySortAsc(type, 1);
    }

    public List<PointRecord> getUserPointRecords(Long userId) {
        return pointRecordRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public CheckInRecord checkIn(Long userId) {
        LocalDate today = LocalDate.now();
        if (checkInRecordRepository.findByUserIdAndCheckInDate(userId, today).isPresent()) {
            return null;
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }

        int continuousDays = calculateContinuousDays(userId, today);
        int basePoints = Math.min(10 + continuousDays, 30);

        MemberLevel level = memberLevelRepository.findById(user.getMemberLevelId()).orElse(null);
        BigDecimal multiplier = level != null ? level.getPointMultiplier() : new BigDecimal("1.0");
        int earnedPoints = new BigDecimal(basePoints).multiply(multiplier).intValue();

        CheckInRecord record = new CheckInRecord();
        record.setUserId(userId);
        record.setCheckInDate(today);
        record.setPoints(earnedPoints);
        record.setContinuousDays(continuousDays + 1);
        checkInRecordRepository.save(record);

        addPoints(userId, earnedPoints, 2, "签到获得积分", null);

        return record;
    }

    private int calculateContinuousDays(Long userId, LocalDate today) {
        int days = 0;
        LocalDate checkDate = today.minusDays(1);
        while (checkInRecordRepository.findByUserIdAndCheckInDate(userId, checkDate).isPresent()) {
            days++;
            checkDate = checkDate.minusDays(1);
        }
        return days;
    }

    public boolean hasCheckedInToday(Long userId) {
        return checkInRecordRepository.findByUserIdAndCheckInDate(userId, LocalDate.now()).isPresent();
    }

    @Transactional
    public void addPoints(Long userId, int points, int type, String description, String bizId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }

        user.setPoints(user.getPoints() + points);
        userRepository.save(user);

        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(points);
        record.setType(type);
        record.setDescription(description);
        record.setBizId(bizId);
        pointRecordRepository.save(record);
    }

    @Transactional
    public boolean exchangeGoods(Long userId, Long goodsId) {
        User user = userRepository.findById(userId).orElse(null);
        PointGoods goods = pointGoodsRepository.findById(goodsId).orElse(null);

        if (user == null || goods == null) {
            return false;
        }

        if (user.getPoints() < goods.getPoints()) {
            return false;
        }

        if (goods.getStock() <= 0) {
            return false;
        }

        user.setPoints(user.getPoints() - goods.getPoints());
        userRepository.save(user);

        goods.setStock(goods.getStock() - 1);
        goods.setExchanged(goods.getExchanged() + 1);
        pointGoodsRepository.save(goods);

        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setPoints(-goods.getPoints());
        record.setType(4);
        record.setDescription("兑换商品：" + goods.getName());
        record.setBizId(goodsId.toString());
        pointRecordRepository.save(record);

        return true;
    }

    public void addPointsForBusiness(Long userId, int basePoints, String businessName, String bizId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return;
        }

        MemberLevel level = memberLevelRepository.findById(user.getMemberLevelId()).orElse(null);
        BigDecimal multiplier = level != null ? level.getPointMultiplier() : new BigDecimal("1.0");
        int earnedPoints = new BigDecimal(basePoints).multiply(multiplier).intValue();

        addPoints(userId, earnedPoints, 1, "办理" + businessName + "获得积分", bizId);
    }

    public void addPointsForReview(Long userId, String bizId) {
        addPoints(userId, 20, 3, "服务评价获得积分", bizId);
    }
}
