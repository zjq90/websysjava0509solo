package com.platform.management.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.management.dto.SalesReportDTO;
import com.platform.management.entity.ExceptionOrder;
import com.platform.management.entity.InvoiceApplication;
import com.platform.management.entity.SalesRecord;
import com.platform.management.mapper.ExceptionOrderMapper;
import com.platform.management.mapper.InvoiceApplicationMapper;
import com.platform.management.mapper.SalesRecordMapper;
import com.platform.management.vo.DashboardVO;
import com.platform.management.vo.SalesReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 销售报表Service
 * 
 * @author platform
 * @version 1.0.0
 */
@Service
public class SalesReportService {

    @Autowired
    private SalesRecordMapper salesRecordMapper;

    @Autowired
    private ExceptionOrderMapper exceptionOrderMapper;

    @Autowired
    private InvoiceApplicationMapper invoiceApplicationMapper;

    /**
     * 获取销售报表数据
     * 
     * @param dto 查询参数
     * @return 报表数据列表
     */
    public List<SalesReportVO> getSalesReport(SalesReportDTO dto) {
        LocalDate startDate = dto.getStartDate();
        LocalDate endDate = dto.getEndDate();
        String dimension = dto.getDimension();
        String payChannel = dto.getPayChannel();

        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        LambdaQueryWrapper<SalesRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.between(SalesRecord::getSaleDate, startDate, endDate);
        
        if (StringUtils.hasText(payChannel)) {
            wrapper.eq(SalesRecord::getPayChannel, payChannel);
        }
        wrapper.orderByAsc(SalesRecord::getSaleDate);

        List<SalesRecord> records = salesRecordMapper.selectList(wrapper);

        Map<String, List<SalesRecord>> groupedRecords = new LinkedHashMap<>();
        
        if ("DAY".equals(dimension)) {
            groupedRecords = records.stream()
                    .collect(Collectors.groupingBy(
                            r -> r.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));
        } else if ("WEEK".equals(dimension)) {
            groupedRecords = records.stream()
                    .collect(Collectors.groupingBy(
                            r -> getWeekLabel(r.getSaleDate()),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));
        } else if ("MONTH".equals(dimension)) {
            groupedRecords = records.stream()
                    .collect(Collectors.groupingBy(
                            r -> r.getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));
        } else if ("YEAR".equals(dimension)) {
            groupedRecords = records.stream()
                    .collect(Collectors.groupingBy(
                            r -> String.valueOf(r.getSaleDate().getYear()),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));
        }

        List<SalesReportVO> result = new ArrayList<>();
        for (Map.Entry<String, List<SalesRecord>> entry : groupedRecords.entrySet()) {
            SalesReportVO vo = new SalesReportVO();
            vo.setTimeLabel(entry.getKey());
            
            List<SalesRecord> list = entry.getValue();
            BigDecimal totalAmount = list.stream()
                    .map(SalesRecord::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Long count = (long) list.size();
            BigDecimal avgAmount = count > 0 
                    ? totalAmount.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            
            vo.setSalesAmount(totalAmount);
            vo.setOrderCount(count);
            vo.setAvgOrderAmount(avgAmount);
            result.add(vo);
        }

        return result;
    }

    /**
     * 获取周标签
     */
    private String getWeekLabel(LocalDate date) {
        int year = date.getYear();
        int week = date.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
        return year + "-W" + String.format("%02d", week);
    }

    /**
     * 获取仪表盘统计数据
     * 
     * @return 仪表盘数据
     */
    public DashboardVO getDashboardStats() {
        DashboardVO vo = new DashboardVO();
        
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();
        LocalDate monthStart = currentMonth.atDay(1);
        LocalDate monthEnd = currentMonth.atEndOfMonth();

        LambdaQueryWrapper<SalesRecord> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.eq(SalesRecord::getSaleDate, today);
        List<SalesRecord> todayRecords = salesRecordMapper.selectList(todayWrapper);
        
        BigDecimal todaySales = todayRecords.stream()
                .map(SalesRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setTodaySales(todaySales);
        vo.setTodayOrders((long) todayRecords.size());

        LambdaQueryWrapper<SalesRecord> monthWrapper = new LambdaQueryWrapper<>();
        monthWrapper.between(SalesRecord::getSaleDate, monthStart, monthEnd);
        List<SalesRecord> monthRecords = salesRecordMapper.selectList(monthWrapper);
        
        BigDecimal monthSales = monthRecords.stream()
                .map(SalesRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setMonthSales(monthSales);
        vo.setMonthOrders((long) monthRecords.size());

        LambdaQueryWrapper<ExceptionOrder> exceptionWrapper = new LambdaQueryWrapper<>();
        exceptionWrapper.in(ExceptionOrder::getHandleStatus, "PENDING", "PROCESSING");
        Long pendingExceptions = exceptionOrderMapper.selectCount(exceptionWrapper);
        vo.setPendingExceptionOrders(pendingExceptions);

        LambdaQueryWrapper<InvoiceApplication> invoiceWrapper = new LambdaQueryWrapper<>();
        invoiceWrapper.eq(InvoiceApplication::getStatus, "PENDING");
        Long pendingInvoices = invoiceApplicationMapper.selectCount(invoiceWrapper);
        vo.setPendingInvoices(pendingInvoices);

        return vo;
    }
}
