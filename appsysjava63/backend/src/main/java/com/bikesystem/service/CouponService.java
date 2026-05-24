package com.bikesystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bikesystem.entity.Coupon;
import com.bikesystem.entity.CreditRecord;
import com.bikesystem.mapper.CouponMapper;
import com.bikesystem.mapper.CreditRecordMapper;
import com.bikesystem.utils.UserContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 优惠券服务类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Service
public class CouponService extends ServiceImpl<CouponMapper, Coupon> {

    @Resource
    private CreditRecordMapper creditRecordMapper;

    /**
     * 获取我的优惠券列表
     */
    public List<Coupon> getMyCoupons(String status) {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon::getUserId, userId);
        
        if (status != null && !status.isEmpty()) {
            wrapper.eq(Coupon::getStatus, status);
        }
        
        wrapper.orderByDesc(Coupon::getCreateTime);
        
        return list(wrapper);
    }

    /**
     * 获取可用优惠券数量
     */
    public Long getAvailableCouponCount() {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon::getUserId, userId)
                .eq(Coupon::getStatus, "UNUSED")
                .gt(Coupon::getExpireTime, LocalDateTime.now());
        
        return count(wrapper);
    }

    /**
     * 获取信用分记录
     */
    public List<CreditRecord> getCreditRecords(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<CreditRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CreditRecord::getUserId, userId)
                .orderByDesc(CreditRecord::getCreateTime);
        
        if (pageNum != null && pageSize != null) {
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<CreditRecord> page = 
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            creditRecordMapper.selectPage(page, wrapper);
            return page.getRecords();
        }
        
        return creditRecordMapper.selectList(wrapper);
    }
}
