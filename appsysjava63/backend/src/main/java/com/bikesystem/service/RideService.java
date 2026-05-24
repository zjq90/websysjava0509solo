package com.bikesystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bikesystem.common.BusinessException;
import com.bikesystem.common.ResultCode;
import com.bikesystem.dto.LockRequest;
import com.bikesystem.dto.RideCostResponse;
import com.bikesystem.dto.UnlockRequest;
import com.bikesystem.entity.*;
import com.bikesystem.mapper.*;
import com.bikesystem.utils.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * 骑行服务类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Service
public class RideService extends ServiceImpl<RideRecordMapper, RideRecord> {

    @Resource
    private BikeMapper bikeMapper;

    @Resource
    private ReservationMapper reservationMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private UserService userService;

    @Resource
    private BikeService bikeService;

    @Value("${bike.pricing.base-price:1.50}")
    private BigDecimal basePrice;

    @Value("${bike.pricing.per-minute-price:0.05}")
    private BigDecimal perMinutePrice;

    @Value("${bike.pricing.peak-multiplier:1.5}")
    private BigDecimal peakMultiplier;

    @Value("${bike.pricing.free-minutes:2}")
    private Integer freeMinutes;

    /**
     * 开锁骑行
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> unlockBike(UnlockRequest request) {
        Long userId = UserContext.getUserId();
        
        userService.checkRidePermission();
        
        RideRecord ongoingRide = baseMapper.selectOngoingRide(userId);
        if (ongoingRide != null) {
            throw new BusinessException(ResultCode.RIDE_ALREADY_ONGOING);
        }
        
        Bike bike;
        if (request.getBikeId() != null) {
            bike = bikeMapper.selectById(request.getBikeId());
        } else if (request.getQrCode() != null) {
            bike = bikeMapper.selectOne(new LambdaQueryWrapper<Bike>()
                    .eq(Bike::getQrCode, request.getQrCode()));
        } else if (request.getBluetoothMac() != null) {
            bike = bikeMapper.selectOne(new LambdaQueryWrapper<Bike>()
                    .eq(Bike::getBluetoothMac, request.getBluetoothMac()));
        } else {
            throw new BusinessException("请提供车辆ID或二维码");
        }
        
        if (bike == null) {
            throw new BusinessException(ResultCode.BIKE_NOT_FOUND);
        }
        
        Reservation activeReservation = reservationMapper.selectActiveReservation(userId);
        if (activeReservation != null && activeReservation.getBikeId().equals(bike.getId())) {
            activeReservation.setStatus("USED");
            reservationMapper.updateById(activeReservation);
        } else {
            bikeService.checkBikeAvailable(bike);
        }
        
        bike.setStatus("IN_USE");
        bikeMapper.updateById(bike);
        
        LocalDateTime now = LocalDateTime.now();
        RideRecord rideRecord = new RideRecord();
        rideRecord.setUserId(userId);
        rideRecord.setBikeId(bike.getId());
        rideRecord.setBikeNo(bike.getBikeNo());
        rideRecord.setStartTime(now);
        rideRecord.setStartLatitude(request.getLatitude());
        rideRecord.setStartLongitude(request.getLongitude());
        rideRecord.setStartAddress(request.getAddress());
        rideRecord.setIsPeakHour(isPeakHour(now) ? 1 : 0);
        rideRecord.setUnlockType(request.getUnlockType());
        rideRecord.setUnlockSuccess(1);
        rideRecord.setStatus("ONGOING");
        baseMapper.insert(rideRecord);
        
        Map<String, Object> result = new HashMap<>();
        result.put("rideRecordId", rideRecord.getId());
        result.put("bikeId", bike.getId());
        result.put("bikeNo", bike.getBikeNo());
        result.put("bikeType", bike.getBikeType());
        result.put("startTime", now);
        result.put("unlockType", request.getUnlockType());
        result.put("isPeakHour", rideRecord.getIsPeakHour() == 1);
        return result;
    }

    /**
     * 锁车结算
     */
    @Transactional(rollbackFor = Exception.class)
    public RideCostResponse lockBike(LockRequest request) {
        Long userId = UserContext.getUserId();
        
        RideRecord rideRecord;
        if (request.getRideRecordId() != null) {
            rideRecord = baseMapper.selectById(request.getRideRecordId());
        } else {
            rideRecord = baseMapper.selectOngoingRide(userId);
        }
        
        if (rideRecord == null || !"ONGOING".equals(rideRecord.getStatus())) {
            throw new BusinessException(ResultCode.RIDE_NOT_ONGOING);
        }
        
        if (!userId.equals(rideRecord.getUserId())) {
            throw new BusinessException("无权操作此骑行记录");
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(rideRecord.getStartTime(), now);
        int durationMinutes = (int) Math.max(1, minutes);
        
        BigDecimal distance = request.getDistanceKm() != null ? request.getDistanceKm() : 
                calculateDistance(rideRecord.getStartLatitude(), rideRecord.getStartLongitude(), 
                        request.getLatitude(), request.getLongitude());
        
        RideCostResponse cost = calculateCost(rideRecord.getStartTime(), now, durationMinutes, 
                rideRecord.getBikeId(), request.getCouponId());
        
        rideRecord.setEndTime(now);
        rideRecord.setEndLatitude(request.getLatitude());
        rideRecord.setEndLongitude(request.getLongitude());
        rideRecord.setEndAddress(request.getAddress());
        rideRecord.setDurationMinutes(durationMinutes);
        rideRecord.setDistanceKm(distance);
        rideRecord.setBasePrice(cost.getBasePrice());
        rideRecord.setTimePrice(cost.getTimePrice());
        rideRecord.setPeakSurcharge(cost.getPeakSurcharge());
        rideRecord.setDiscountAmount(cost.getDiscountAmount());
        rideRecord.setCouponId(request.getCouponId());
        rideRecord.setTotalAmount(cost.getTotalAmount());
        rideRecord.setActualAmount(cost.getActualAmount());
        rideRecord.setStatus("COMPLETED");
        
        User user = userMapper.selectById(userId);
        boolean isPostpaid = false;
        
        if (user.getBalance().compareTo(cost.getActualAmount()) >= 0) {
            user.setBalance(user.getBalance().subtract(cost.getActualAmount()));
            rideRecord.setPaymentStatus("PAID");
            rideRecord.setPaymentMethod("BALANCE");
            rideRecord.setPaymentTime(now);
        } else {
            isPostpaid = true;
            rideRecord.setPaymentStatus("UNPAID");
        }
        userMapper.updateById(user);
        
        baseMapper.updateById(rideRecord);
        
        Bike bike = bikeMapper.selectById(rideRecord.getBikeId());
        if (bike != null) {
            bike.setStatus("AVAILABLE");
            bike.setLatitude(request.getLatitude());
            bike.setLongitude(request.getLongitude());
            bike.setLocationAddress(request.getAddress());
            bike.setTotalRides(bike.getTotalRides() + 1);
            bike.setTotalKm(bike.getTotalKm().add(distance));
            bikeMapper.updateById(bike);
        }
        
        if (cost.getIsPeakHour()) {
            userService.changeCreditScore(userId, 1, "规范停车，文明骑行", "RIDE", rideRecord.getId());
        }
        
        cost.setRideRecordId(rideRecord.getId());
        cost.setStartTime(rideRecord.getStartTime());
        cost.setEndTime(now);
        cost.setDurationMinutes(durationMinutes);
        cost.setDistanceKm(distance);
        cost.setPaymentStatus(rideRecord.getPaymentStatus());
        
        return cost;
    }

    /**
     * 计算费用
     */
    public RideCostResponse calculateCost(LocalDateTime startTime, LocalDateTime endTime, 
                                           int durationMinutes, Long bikeId, Long couponId) {
        boolean isPeak = isPeakHour(startTime);
        Bike bike = bikeMapper.selectById(bikeId);
        String bikeType = bike != null ? bike.getBikeType() : "STANDARD";
        
        BigDecimal typeBasePrice = basePrice;
        BigDecimal typePerMinutePrice = perMinutePrice;
        if ("ELECTRIC".equals(bikeType)) {
            typeBasePrice = new BigDecimal("2.00");
            typePerMinutePrice = new BigDecimal("0.08");
        } else if ("ASSIST".equals(bikeType)) {
            typeBasePrice = new BigDecimal("2.50");
            typePerMinutePrice = new BigDecimal("0.10");
        }
        
        int chargeableMinutes = Math.max(0, durationMinutes - freeMinutes);
        
        BigDecimal timeCost = typePerMinutePrice.multiply(BigDecimal.valueOf(chargeableMinutes));
        BigDecimal totalBeforePeak = typeBasePrice.add(timeCost);
        
        BigDecimal peakSurcharge = BigDecimal.ZERO;
        if (isPeak) {
            peakSurcharge = totalBeforePeak.multiply(peakMultiplier.subtract(BigDecimal.ONE));
        }
        
        BigDecimal totalAmount = totalBeforePeak.add(peakSurcharge);
        
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (couponId != null) {
            Coupon coupon = couponMapper.selectById(couponId);
            if (coupon != null && "UNUSED".equals(coupon.getStatus()) && 
                    LocalDateTime.now().isBefore(coupon.getExpireTime())) {
                
                if (totalAmount.compareTo(coupon.getMinAmount()) >= 0) {
                    if ("DISCOUNT".equals(coupon.getCouponType())) {
                        discountAmount = totalAmount.multiply(BigDecimal.ONE.subtract(coupon.getDiscountValue()));
                        if (coupon.getMaxDiscount() != null && discountAmount.compareTo(coupon.getMaxDiscount()) > 0) {
                            discountAmount = coupon.getMaxDiscount();
                        }
                    } else if ("FIXED".equals(coupon.getCouponType())) {
                        discountAmount = coupon.getDiscountValue();
                    } else if ("FREE".equals(coupon.getCouponType())) {
                        discountAmount = totalAmount;
                    }
                    
                    if (discountAmount.compareTo(totalAmount) > 0) {
                        discountAmount = totalAmount;
                    }
                    
                    coupon.setStatus("USED");
                    coupon.setUseTime(LocalDateTime.now());
                    couponMapper.updateById(coupon);
                }
            }
        }
        
        BigDecimal actualAmount = totalAmount.subtract(discountAmount);
        if (actualAmount.compareTo(BigDecimal.ZERO) < 0) {
            actualAmount = BigDecimal.ZERO;
        }
        
        RideCostResponse response = new RideCostResponse();
        response.setBasePrice(typeBasePrice);
        response.setTimePrice(timeCost);
        response.setPeakSurcharge(peakSurcharge);
        response.setDiscountAmount(discountAmount);
        response.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        response.setActualAmount(actualAmount.setScale(2, RoundingMode.HALF_UP));
        response.setIsPeakHour(isPeak);
        
        return response;
    }

    /**
     * 获取当前进行中的骑行
     */
    public RideRecord getOngoingRide() {
        Long userId = UserContext.getUserId();
        return baseMapper.selectOngoingRide(userId);
    }

    /**
     * 获取骑行实时费用
     */
    public RideCostResponse getCurrentCost() {
        Long userId = UserContext.getUserId();
        RideRecord rideRecord = baseMapper.selectOngoingRide(userId);
        if (rideRecord == null) {
            throw new BusinessException(ResultCode.RIDE_NOT_ONGOING);
        }
        
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(rideRecord.getStartTime(), now);
        int durationMinutes = (int) Math.max(1, minutes);
        
        return calculateCost(rideRecord.getStartTime(), now, durationMinutes, rideRecord.getBikeId(), null);
    }

    /**
     * 判断是否高峰时段
     */
    private boolean isPeakHour(LocalDateTime time) {
        DayOfWeek dayOfWeek = time.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
            return false;
        }
        
        LocalTime localTime = time.toLocalTime();
        LocalTime morningStart = LocalTime.of(7, 0);
        LocalTime morningEnd = LocalTime.of(9, 0);
        LocalTime eveningStart = LocalTime.of(17, 0);
        LocalTime eveningEnd = LocalTime.of(19, 0);
        
        return (localTime.isAfter(morningStart) && localTime.isBefore(morningEnd)) ||
                (localTime.isAfter(eveningStart) && localTime.isBefore(eveningEnd));
    }

    /**
     * 计算两点之间距离(简化版)
     */
    private BigDecimal calculateDistance(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2) {
        if (lat1 == null || lng1 == null || lat2 == null || lng2 == null) {
            return BigDecimal.ZERO;
        }
        
        double latDiff = Math.abs(lat1.doubleValue() - lat2.doubleValue());
        double lngDiff = Math.abs(lng1.doubleValue() - lng2.doubleValue());
        double distance = Math.sqrt(latDiff * latDiff + lngDiff * lngDiff) * 111;
        
        return BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_UP);
    }
}
