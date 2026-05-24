package com.bikesystem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bikesystem.common.BusinessException;
import com.bikesystem.common.ResultCode;
import com.bikesystem.dto.FaultReportRequest;
import com.bikesystem.entity.Bike;
import com.bikesystem.entity.Coupon;
import com.bikesystem.entity.FaultReport;
import com.bikesystem.mapper.BikeMapper;
import com.bikesystem.mapper.CouponMapper;
import com.bikesystem.mapper.FaultReportMapper;
import com.bikesystem.utils.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 故障上报服务类
 * 
 * @author BikeSystem
 * @since 2024-01-01
 */
@Service
public class FaultReportService extends ServiceImpl<FaultReportMapper, FaultReport> {

    @Resource
    private BikeMapper bikeMapper;

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private UserService userService;

    /**
     * 提交故障上报
     */
    @Transactional(rollbackFor = Exception.class)
    public FaultReport submitReport(FaultReportRequest request) {
        Long userId = UserContext.getUserId();
        
        Bike bike = bikeMapper.selectById(request.getBikeId());
        if (bike == null) {
            throw new BusinessException(ResultCode.BIKE_NOT_FOUND);
        }
        
        FaultReport report = new FaultReport();
        report.setUserId(userId);
        report.setBikeId(request.getBikeId());
        report.setBikeNo(bike.getBikeNo());
        report.setFaultType(request.getFaultType());
        report.setFaultDescription(request.getFaultDescription());
        report.setImageUrls(request.getImageUrls());
        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        report.setStatus("PENDING");
        save(report);
        
        bike.setStatus("FAULTY");
        bikeMapper.updateById(bike);
        
        return report;
    }

    /**
     * 获取我的上报记录
     */
    public List<FaultReport> getMyReports(Integer pageNum, Integer pageSize) {
        Long userId = UserContext.getUserId();
        
        LambdaQueryWrapper<FaultReport> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FaultReport::getUserId, userId)
                .orderByDesc(FaultReport::getCreateTime);
        
        if (pageNum != null && pageSize != null) {
            Page<FaultReport> page = new Page<>(pageNum, pageSize);
            IPage<FaultReport> result = page(page, wrapper);
            return result.getRecords();
        }
        
        return list(wrapper);
    }

    /**
     * 获取上报详情
     */
    public FaultReport getReportDetail(Long reportId) {
        Long userId = UserContext.getUserId();
        
        FaultReport report = getById(reportId);
        if (report == null || !userId.equals(report.getUserId())) {
            throw new BusinessException("上报记录不存在");
        }
        
        return report;
    }

    /**
     * 处理故障上报(后台)
     */
    @Transactional(rollbackFor = Exception.class)
    public void processReport(Long reportId, String status, BigDecimal rewardAmount, String remark) {
        FaultReport report = getById(reportId);
        if (report == null) {
            throw new BusinessException("上报记录不存在");
        }
        
        report.setStatus(status);
        report.setHandleTime(LocalDateTime.now());
        report.setHandleRemark(remark);
        
        if ("RESOLVED".equals(status) && rewardAmount != null && rewardAmount.compareTo(BigDecimal.ZERO) > 0) {
            Coupon coupon = new Coupon();
            coupon.setUserId(report.getUserId());
            coupon.setCouponName("故障上报奖励券");
            coupon.setCouponType("FIXED");
            coupon.setDiscountValue(rewardAmount);
            coupon.setMinAmount(BigDecimal.ZERO);
            coupon.setIssueTime(LocalDateTime.now());
            coupon.setExpireTime(LocalDateTime.now().plusMonths(1));
            coupon.setStatus("UNUSED");
            coupon.setSource("FAULT_REPORT");
            couponMapper.insert(coupon);
            
            report.setRewardCouponId(coupon.getId());
            report.setRewardAmount(rewardAmount);
            
            userService.changeCreditScore(report.getUserId(), 5, "上报车辆故障，感谢您的反馈", 
                    "FAULT", reportId);
        }
        
        updateById(report);
    }
}
