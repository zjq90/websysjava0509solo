package com.bikesystem.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bikesystem.entity.Bike;
import com.bikesystem.entity.Reservation;
import com.bikesystem.mapper.BikeMapper;
import com.bikesystem.mapper.ReservationMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Component
public class ScheduledTasks {

    @Resource
    private ReservationMapper reservationMapper;

    @Resource
    private BikeMapper bikeMapper;

    /**
     * 每分钟检查过期预约
     */
    @Scheduled(fixedRate = 60000)
    public void checkExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        
        LambdaQueryWrapper<Reservation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Reservation::getStatus, "ACTIVE")
                .lt(Reservation::getExpireTime, now);
        
        List<Reservation> expiredReservations = reservationMapper.selectList(wrapper);
        
        for (Reservation reservation : expiredReservations) {
            reservation.setStatus("EXPIRED");
            reservationMapper.updateById(reservation);
            
            Bike bike = bikeMapper.selectById(reservation.getBikeId());
            if (bike != null && "RESERVED".equals(bike.getStatus())) {
                bike.setStatus("AVAILABLE");
                bikeMapper.updateById(bike);
            }
        }
    }

    /**
     * 每小时检查过期优惠券
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void checkExpiredCoupons() {
        // 这里可以添加检查过期优惠券的逻辑
    }

    /**
     * 每天凌晨清理动态二维码
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanDynamicQrCodes() {
        // 这里可以添加清理动态二维码的逻辑
    }
}
