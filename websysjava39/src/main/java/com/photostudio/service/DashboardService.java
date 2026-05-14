package com.photostudio.service;

import com.photostudio.dto.ConversionFunnelDTO;
import com.photostudio.dto.EmployeePerformanceDTO;
import com.photostudio.dto.FinanceReportDTO;
import com.photostudio.dto.SalesDashboardDTO;
import com.photostudio.entity.Employee;
import com.photostudio.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 看板服务
 * 提供销售业绩、转化漏斗、员工绩效、财务报表等数据统计
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Service
public class DashboardService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private FinanceRecordRepository financeRecordRepository;
    
    @Autowired
    private PhotoEditRecordRepository photoEditRecordRepository;
    
    @Autowired
    private StoreRepository storeRepository;

    /**
     * 获取销售业绩看板数据
     */
    public SalesDashboardDTO getSalesDashboard(Long storeId, Long salesId, String packageType, 
                                               LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime = startDate != null ? startDate.atStartOfDay() : LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endTime = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.now();
        
        // 上一期时间范围
        LocalDateTime lastStartTime = startTime.minusMonths(1);
        LocalDateTime lastEndTime = endTime.minusMonths(1);
        
        List<String> completedStatuses = Arrays.asList("DELIVERED");
        
        // 当前期数据
        BigDecimal totalRevenue = orderRepository.sumAmountByStatusAndTimeRange(completedStatuses, startTime, endTime);
        Long totalOrders = orderRepository.countByStatusAndTimeRange("DELIVERED", startTime, endTime);
        Long consultingCount = orderRepository.countByStatusAndTimeRange("CONSULTING", startTime, endTime);
        
        // 上期数据
        BigDecimal lastTotalRevenue = orderRepository.sumAmountByStatusAndTimeRange(completedStatuses, lastStartTime, lastEndTime);
        Long lastTotalOrders = orderRepository.countByStatusAndTimeRange("DELIVERED", lastStartTime, lastEndTime);
        Long lastConsultingCount = orderRepository.countByStatusAndTimeRange("CONSULTING", lastStartTime, lastEndTime);
        
        SalesDashboardDTO dto = new SalesDashboardDTO();
        dto.setTotalRevenue(totalRevenue);
        dto.setTotalOrders(totalOrders.intValue());
        
        // 客单价
        if (totalOrders > 0) {
            dto.setAvgOrderValue(totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP));
        } else {
            dto.setAvgOrderValue(BigDecimal.ZERO);
        }
        
        // 转化率
        if (consultingCount > 0) {
            dto.setConversionRate(BigDecimal.valueOf(totalOrders)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(consultingCount), 2, RoundingMode.HALF_UP));
        } else {
            dto.setConversionRate(BigDecimal.ZERO);
        }
        
        // 同比增长率
        dto.setRevenueGrowthRate(calculateGrowthRate(totalRevenue, lastTotalRevenue));
        dto.setOrderGrowthRate(calculateGrowthRate(BigDecimal.valueOf(totalOrders), BigDecimal.valueOf(lastTotalOrders)));
        
        BigDecimal lastAvgOrder = lastTotalOrders > 0 ? lastTotalRevenue.divide(BigDecimal.valueOf(lastTotalOrders), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        dto.setAvgOrderGrowthRate(calculateGrowthRate(dto.getAvgOrderValue(), lastAvgOrder));
        
        BigDecimal lastConversion = lastConsultingCount > 0 ? BigDecimal.valueOf(lastTotalOrders).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(lastConsultingCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        dto.setConversionGrowthRate(calculateGrowthRate(dto.getConversionRate(), lastConversion));
        
        // 目标完成率（假设目标为上个月的110%）
        BigDecimal target = lastTotalRevenue.multiply(BigDecimal.valueOf(1.1));
        if (target.compareTo(BigDecimal.ZERO) > 0) {
            dto.setTargetCompletionRate(totalRevenue.multiply(BigDecimal.valueOf(100)).divide(target, 2, RoundingMode.HALF_UP));
        } else {
            dto.setTargetCompletionRate(BigDecimal.ZERO);
        }
        
        return dto;
    }
    
    /**
     * 计算增长率
     */
    private BigDecimal calculateGrowthRate(BigDecimal current, BigDecimal last) {
        if (last.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) > 0 ? BigDecimal.valueOf(100) : BigDecimal.ZERO;
        }
        return current.subtract(last)
                .multiply(BigDecimal.valueOf(100))
                .divide(last, 2, RoundingMode.HALF_UP);
    }

    /**
     * 获取客户转化漏斗数据
     */
    public ConversionFunnelDTO getConversionFunnel(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime = startDate != null ? startDate.atStartOfDay() : LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endTime = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.now();
        
        ConversionFunnelDTO dto = new ConversionFunnelDTO();
        
        // 各环节订单数量
        Long consultingCount = orderRepository.countByStatusAndTimeRange("CONSULTING", startTime, endTime) +
                              orderRepository.countByStatusAndTimeRange("ORDERED", startTime, endTime) +
                              orderRepository.countByStatusAndTimeRange("SHOOTING", startTime, endTime) +
                              orderRepository.countByStatusAndTimeRange("SHOOT_COMPLETED", startTime, endTime) +
                              orderRepository.countByStatusAndTimeRange("SELECTING", startTime, endTime) +
                              orderRepository.countByStatusAndTimeRange("EDITING", startTime, endTime) +
                              orderRepository.countByStatusAndTimeRange("DELIVERED", startTime, endTime);
        Long orderedCount = orderRepository.countByStatusAndTimeRange("ORDERED", startTime, endTime) +
                           orderRepository.countByStatusAndTimeRange("SHOOTING", startTime, endTime) +
                           orderRepository.countByStatusAndTimeRange("SHOOT_COMPLETED", startTime, endTime) +
                           orderRepository.countByStatusAndTimeRange("SELECTING", startTime, endTime) +
                           orderRepository.countByStatusAndTimeRange("EDITING", startTime, endTime) +
                           orderRepository.countByStatusAndTimeRange("DELIVERED", startTime, endTime);
        Long shootCompletedCount = orderRepository.countByStatusAndTimeRange("SHOOT_COMPLETED", startTime, endTime) +
                                  orderRepository.countByStatusAndTimeRange("SELECTING", startTime, endTime) +
                                  orderRepository.countByStatusAndTimeRange("EDITING", startTime, endTime) +
                                  orderRepository.countByStatusAndTimeRange("DELIVERED", startTime, endTime);
        Long deliveredCount = orderRepository.countByStatusAndTimeRange("DELIVERED", startTime, endTime);
        
        dto.setConsultingCount(consultingCount.intValue());
        dto.setOrderedCount(orderedCount.intValue());
        dto.setShootCompletedCount(shootCompletedCount.intValue());
        dto.setDeliveredCount(deliveredCount.intValue());
        
        // 各环节转化率
        if (consultingCount > 0) {
            dto.setConsultToOrderRate(BigDecimal.valueOf(orderedCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(consultingCount), 2, RoundingMode.HALF_UP));
        } else {
            dto.setConsultToOrderRate(BigDecimal.ZERO);
        }
        
        if (orderedCount > 0) {
            dto.setOrderToShootRate(BigDecimal.valueOf(shootCompletedCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(orderedCount), 2, RoundingMode.HALF_UP));
        } else {
            dto.setOrderToShootRate(BigDecimal.ZERO);
        }
        
        if (shootCompletedCount > 0) {
            dto.setShootToDeliverRate(BigDecimal.valueOf(deliveredCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(shootCompletedCount), 2, RoundingMode.HALF_UP));
        } else {
            dto.setShootToDeliverRate(BigDecimal.ZERO);
        }
        
        if (consultingCount > 0) {
            dto.setTotalConversionRate(BigDecimal.valueOf(deliveredCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(consultingCount), 2, RoundingMode.HALF_UP));
        } else {
            dto.setTotalConversionRate(BigDecimal.ZERO);
        }
        
        // 老客户复购率
        Long totalOrders = consultingCount;
        Long oldCustomerOrders = orderRepository.countOldCustomerOrders();
        if (totalOrders > 0) {
            dto.setRepeatPurchaseRate(BigDecimal.valueOf(oldCustomerOrders)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP));
        } else {
            dto.setRepeatPurchaseRate(BigDecimal.ZERO);
        }
        
        // 转介绍率
        Long referralOrders = orderRepository.countReferralCustomerOrders();
        if (totalOrders > 0) {
            dto.setReferralRate(BigDecimal.valueOf(referralOrders)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP));
        } else {
            dto.setReferralRate(BigDecimal.ZERO);
        }
        
        return dto;
    }

    /**
     * 获取员工绩效排行
     */
    public List<EmployeePerformanceDTO> getEmployeePerformance(String position, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startTime = startDate != null ? startDate.atStartOfDay() : LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endTime = endDate != null ? endDate.atTime(23, 59, 59) : LocalDateTime.now();
        
        List<Employee> employees;
        if (position != null) {
            employees = employeeRepository.findByPositionAndActive(position, true);
        } else {
            employees = employeeRepository.findByActive(true);
        }
        
        List<EmployeePerformanceDTO> performanceList = new ArrayList<>();
        
        for (Employee employee : employees) {
            EmployeePerformanceDTO dto = new EmployeePerformanceDTO();
            dto.setEmployeeId(employee.getId());
            dto.setEmployeeName(employee.getName());
            dto.setPosition(employee.getPosition());
            
            if (!"PHOTO_EDITOR".equals(employee.getPosition())) {
                // 非修图师统计订单数、评分、金额
                Long orderCount = getOrderCountByPosition(employee.getId(), employee.getPosition(), startTime, endTime);
                BigDecimal totalRevenue = getRevenueByPosition(employee.getId(), employee.getPosition(), startTime, endTime);
                BigDecimal avgRating = getRatingByPosition(employee.getId(), employee.getPosition(), startTime, endTime);
                
                dto.setOrderCount(orderCount.intValue());
                dto.setTotalRevenue(totalRevenue);
                dto.setAvgRating(avgRating);
            } else {
                // 修图师统计修图数量和返修率
                Integer photoCount = photoEditRecordRepository.sumPhotoCountByEditorAndTimeRange(
                    employee.getId(), startTime, endTime);
                Integer reworkCount = photoEditRecordRepository.sumReworkCountByEditorAndTimeRange(
                    employee.getId(), startTime, endTime);
                
                dto.setEditPhotoCount(photoCount);
                if (photoCount > 0) {
                    dto.setReworkRate(BigDecimal.valueOf(reworkCount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(photoCount), 2, RoundingMode.HALF_UP));
                } else {
                    dto.setReworkRate(BigDecimal.ZERO);
                }
            }
            
            performanceList.add(dto);
        }
        
        // 排序
        performanceList.sort(Comparator.comparing(
            (EmployeePerformanceDTO dto) -> dto.getTotalRevenue() != null ? dto.getTotalRevenue() : BigDecimal.ZERO
        ).reversed());
        
        // 设置排名
        int rank = 1;
        for (EmployeePerformanceDTO dto : performanceList) {
            dto.setRank(rank++);
        }
        
        return performanceList;
    }
    
    /**
     * 根据岗位获取订单数量
     */
    private Long getOrderCountByPosition(Long employeeId, String position, LocalDateTime startTime, LocalDateTime endTime) {
        switch (position) {
            case "SALES":
                return orderRepository.countBySalesId(employeeId, startTime, endTime);
            case "PHOTOGRAPHER":
                return orderRepository.countByPhotographerId(employeeId, startTime, endTime);
            case "MAKEUP_ARTIST":
                return orderRepository.countByMakeupArtistId(employeeId, startTime, endTime);
            case "PHOTO_SELECTOR":
                return orderRepository.countByPhotoSelectorId(employeeId, startTime, endTime);
            case "PHOTO_EDITOR":
                return orderRepository.countByPhotoEditorId(employeeId, startTime, endTime);
            default:
                return 0L;
        }
    }
    
    /**
     * 根据岗位获取订单金额
     */
    private BigDecimal getRevenueByPosition(Long employeeId, String position, LocalDateTime startTime, LocalDateTime endTime) {
        switch (position) {
            case "SALES":
                return orderRepository.sumAmountBySalesId(employeeId, startTime, endTime);
            case "PHOTOGRAPHER":
                return orderRepository.sumAmountByPhotographerId(employeeId, startTime, endTime);
            case "MAKEUP_ARTIST":
                return orderRepository.sumAmountByMakeupArtistId(employeeId, startTime, endTime);
            case "PHOTO_SELECTOR":
                return orderRepository.sumAmountByPhotoSelectorId(employeeId, startTime, endTime);
            default:
                return BigDecimal.ZERO;
        }
    }
    
    /**
     * 根据岗位获取平均评分
     */
    private BigDecimal getRatingByPosition(Long employeeId, String position, LocalDateTime startTime, LocalDateTime endTime) {
        switch (position) {
            case "SALES":
                return orderRepository.avgRatingBySalesId(employeeId, startTime, endTime);
            case "PHOTOGRAPHER":
                return orderRepository.avgRatingByPhotographerId(employeeId, startTime, endTime);
            case "MAKEUP_ARTIST":
                return orderRepository.avgRatingByMakeupArtistId(employeeId, startTime, endTime);
            case "PHOTO_SELECTOR":
                return orderRepository.avgRatingByPhotoSelectorId(employeeId, startTime, endTime);
            default:
                return BigDecimal.ZERO;
        }
    }

    /**
     * 获取财务报表
     */
    public FinanceReportDTO getFinanceReport(String period, Long storeId) {
        LocalDate now = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;
        
        switch (period != null ? period : "MONTHLY") {
            case "DAILY":
                startDate = now;
                endDate = now;
                break;
            case "WEEKLY":
                startDate = now.minusWeeks(1);
                endDate = now;
                break;
            case "MONTHLY":
            default:
                startDate = now.withDayOfMonth(1);
                endDate = YearMonth.from(now).atEndOfMonth();
                break;
        }
        
        FinanceReportDTO dto = new FinanceReportDTO();
        dto.setReportDate(now);
        
        // 收入
        dto.setTotalIncome(financeRecordRepository.sumByTypeAndDateRangeAndStore("INCOME", startDate, endDate, storeId));
        dto.setOrderIncome(financeRecordRepository.sumByCategoryAndDateRangeAndStore("ORDER_PAYMENT", startDate, endDate, storeId));
        
        // 支出
        dto.setTotalExpense(financeRecordRepository.sumByTypeAndDateRangeAndStore("EXPENSE", startDate, endDate, storeId));
        dto.setSalaryExpense(financeRecordRepository.sumByCategoryAndDateRangeAndStore("SALARY", startDate, endDate, storeId));
        dto.setRentExpense(financeRecordRepository.sumByCategoryAndDateRangeAndStore("RENT", startDate, endDate, storeId));
        dto.setMaterialsExpense(financeRecordRepository.sumByCategoryAndDateRangeAndStore("MATERIALS", startDate, endDate, storeId));
        dto.setOtherExpense(financeRecordRepository.sumByCategoryAndDateRangeAndStore("OTHER", startDate, endDate, storeId));
        
        // 净利润
        dto.setNetProfit(dto.getTotalIncome().subtract(dto.getTotalExpense()));
        
        return dto;
    }
}
