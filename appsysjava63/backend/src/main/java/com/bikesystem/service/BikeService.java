package com.bikesystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bikesystem.common.BusinessException;
import com.bikesystem.common.ResultCode;
import com.bikesystem.entity.Bike;
import com.bikesystem.entity.Reservation;
import com.bikesystem.mapper.BikeMapper;
import com.bikesystem.mapper.ReservationMapper;
import com.bikesystem.utils.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 车辆服务类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Service
public class BikeService extends ServiceImpl<BikeMapper, Bike> {

    @Resource
    private ReservationMapper reservationMapper;

    @Value("${bike.reservation.hold-minutes:15}")
    private Integer holdMinutes;

    /**
     * 获取附近可用车辆
     */
    public List<Bike> getNearbyBikes(BigDecimal lat, BigDecimal lng, String bikeType, 
                                       Integer minBattery, Integer limit) {
        BigDecimal range = new BigDecimal("0.02");
        BigDecimal minLat = lat.subtract(range);
        BigDecimal maxLat = lat.add(range);
        BigDecimal minLng = lng.subtract(range);
        BigDecimal maxLng = lng.add(range);
        
        List<Bike> bikes = baseMapper.findNearbyAvailableBikes(lat, lng, minLat, maxLat, minLng, maxLng, 
                limit != null ? limit : 50);
        
        if (bikeType != null && !bikeType.isEmpty()) {
            bikes.removeIf(bike -> !bikeType.equals(bike.getBikeType()));
        }
        
        if (minBattery != null) {
            bikes.removeIf(bike -> bike.getBatteryLevel() != null && bike.getBatteryLevel() < minBattery);
        }
        
        return bikes;
    }

    /**
     * 获取车辆详情
     */
    public Bike getBikeDetail(Long bikeId) {
        Bike bike = getById(bikeId);
        if (bike == null) {
            throw new BusinessException(ResultCode.BIKE_NOT_FOUND);
        }
        return bike;
    }

    /**
     * 根据二维码获取车辆
     */
    public Bike getBikeByQrCode(String qrCode) {
        Bike bike = getOne(new LambdaQueryWrapper<Bike>().eq(Bike::getQrCode, qrCode));
        if (bike == null) {
            throw new BusinessException(ResultCode.BIKE_NOT_FOUND);
        }
        return bike;
    }

    /**
     * 预约车辆
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> reserveBike(Long bikeId) {
        Long userId = UserContext.getUserId();
        
        Reservation activeReservation = reservationMapper.selectActiveReservation(userId);
        if (activeReservation != null) {
            throw new BusinessException(ResultCode.ALREADY_HAS_RESERVATION);
        }
        
        Bike bike = getById(bikeId);
        if (bike == null) {
            throw new BusinessException(ResultCode.BIKE_NOT_FOUND);
        }
        
        if (!"AVAILABLE".equals(bike.getStatus())) {
            if ("IN_USE".equals(bike.getStatus())) {
                throw new BusinessException(ResultCode.BIKE_IN_USE);
            } else if ("RESERVED".equals(bike.getStatus())) {
                throw new BusinessException(ResultCode.BIKE_RESERVED);
            } else if ("FAULTY".equals(bike.getStatus())) {
                throw new BusinessException(ResultCode.BIKE_FAULTY);
            } else {
                throw new BusinessException(ResultCode.BIKE_NOT_AVAILABLE);
            }
        }
        
        bike.setStatus("RESERVED");
        updateById(bike);
        
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation();
        reservation.setUserId(userId);
        reservation.setBikeId(bikeId);
        reservation.setBikeNo(bike.getBikeNo());
        reservation.setReserveTime(now);
        reservation.setExpireTime(now.plusMinutes(holdMinutes));
        reservation.setStatus("ACTIVE");
        reservationMapper.insert(reservation);
        
        Map<String, Object> result = new HashMap<>();
        result.put("reservationId", reservation.getId());
        result.put("bikeId", bikeId);
        result.put("bikeNo", bike.getBikeNo());
        result.put("reserveTime", reservation.getReserveTime());
        result.put("expireTime", reservation.getExpireTime());
        result.put("holdMinutes", holdMinutes);
        return result;
    }

    /**
     * 取消预约
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancelReservation(Long reservationId) {
        Long userId = UserContext.getUserId();
        
        Reservation reservation = reservationMapper.selectById(reservationId);
        if (reservation == null || !userId.equals(reservation.getUserId())) {
            throw new BusinessException("预约记录不存在");
        }
        
        if (!"ACTIVE".equals(reservation.getStatus())) {
            throw new BusinessException("预约已过期或已取消");
        }
        
        reservation.setStatus("CANCELLED");
        reservation.setCancelTime(LocalDateTime.now());
        reservationMapper.updateById(reservation);
        
        Bike bike = getById(reservation.getBikeId());
        if (bike != null && "RESERVED".equals(bike.getStatus())) {
            bike.setStatus("AVAILABLE");
            updateById(bike);
        }
    }

    /**
     * 获取用户当前预约
     */
    public Reservation getCurrentReservation() {
        Long userId = UserContext.getUserId();
        return reservationMapper.selectActiveReservation(userId);
    }

    /**
     * 生成动态二维码
     */
    public Map<String, Object> generateDynamicQrCode(Long bikeId) {
        Bike bike = getById(bikeId);
        if (bike == null) {
            throw new BusinessException(ResultCode.BIKE_NOT_FOUND);
        }
        
        String qrContent = "bike://" + bike.getBikeNo() + "?t=" + System.currentTimeMillis() + "&r=" + 
                ((int) (Math.random() * 100000));
        
        Map<String, Object> result = new HashMap<>();
        result.put("bikeId", bikeId);
        result.put("bikeNo", bike.getBikeNo());
        result.put("qrCodeContent", qrContent);
        result.put("generateTime", LocalDateTime.now());
        result.put("expireSeconds", 60);
        return result;
    }

    /**
     * 检查车辆状态
     */
    public void checkBikeAvailable(Bike bike) {
        if (!"AVAILABLE".equals(bike.getStatus())) {
            if ("IN_USE".equals(bike.getStatus())) {
                throw new BusinessException(ResultCode.BIKE_IN_USE);
            } else if ("RESERVED".equals(bike.getStatus())) {
                throw new BusinessException(ResultCode.BIKE_RESERVED);
            } else if ("FAULTY".equals(bike.getStatus())) {
                throw new BusinessException(ResultCode.BIKE_FAULTY);
            } else {
                throw new BusinessException(ResultCode.BIKE_NOT_AVAILABLE);
            }
        }
    }
}
