package com.secondhand.service;

import com.secondhand.entity.Order;
import com.secondhand.entity.Product;
import com.secondhand.repository.OrderRepository;
import com.secondhand.repository.ProductRepository;
import com.secondhand.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class StatisticsService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getDailyTrend(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            LocalDateTime start = current.atStartOfDay();
            LocalDateTime end = current.atTime(LocalTime.MAX);
            BigDecimal amount = orderRepository.sumTotalAmountByCreateTimeBetween(start, end);
            dates.add(current.toString());
            amounts.add(amount);
            current = current.plusDays(1);
        }

        result.put("dates", dates);
        result.put("amounts", amounts);
        return result;
    }

    public Map<String, Object> getWeeklyTrend(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        List<String> weeks = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();

        LocalDate current = startDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        while (!current.isAfter(endDate)) {
            LocalDateTime start = current.atStartOfDay();
            LocalDateTime end = current.plusDays(6).atTime(LocalTime.MAX);
            BigDecimal amount = orderRepository.sumTotalAmountByCreateTimeBetween(start, end);
            weeks.add(current.toString() + " ~ " + current.plusDays(6).toString());
            amounts.add(amount);
            current = current.plusWeeks(1);
        }

        result.put("weeks", weeks);
        result.put("amounts", amounts);
        return result;
    }

    public Map<String, Object> getMonthlyTrend(int year) {
        Map<String, Object> result = new HashMap<>();
        List<String> months = new ArrayList<>();
        List<BigDecimal> amounts = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = endDate.atTime(LocalTime.MAX);
            BigDecimal amount = orderRepository.sumTotalAmountByCreateTimeBetween(start, end);
            months.add(year + "-" + String.format("%02d", month));
            amounts.add(amount);
        }

        result.put("months", months);
        result.put("amounts", amounts);
        return result;
    }

    public List<Map<String, Object>> getProductSalesRanking(int topN) {
        List<Product> products = productRepository.findByStatus("ON_SALE");
        List<Map<String, Object>> ranking = new ArrayList<>();

        for (Product product : products) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", product.getId());
            item.put("name", product.getName());
            item.put("price", product.getPrice());
            item.put("stock", product.getStock());
            ranking.add(item);
        }

        ranking.sort((a, b) -> Integer.compare((Integer) b.get("stock"), (Integer) a.get("stock")));
        return ranking.subList(0, Math.min(topN, ranking.size()));
    }

    public Map<String, Object> getUserGrowthAnalysis(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            LocalDateTime start = current.atStartOfDay();
            LocalDateTime end = current.atTime(LocalTime.MAX);
            Long count = userRepository.countByCreateTimeBetween(start, end);
            dates.add(current.toString());
            counts.add(count);
            current = current.plusDays(1);
        }

        result.put("dates", dates);
        result.put("counts", counts);
        return result;
    }

    public byte[] exportStatisticsToExcel(Map<String, Object> statistics) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("统计数据");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("日期");
            headerRow.createCell(1).setCellValue("交易额");

            List<String> dates = (List<String>) statistics.get("dates");
            List<BigDecimal> amounts = (List<BigDecimal>) statistics.get("amounts");

            int rowNum = 1;
            for (int i = 0; i < dates.size(); i++) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dates.get(i));
                row.createCell(1).setCellValue(amounts.get(i).doubleValue());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

}