package com.gameplatform.service;

import com.gameplatform.entity.RevenueStats;
import com.gameplatform.repository.RevenueStatsRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RevenueStatsService {
    @Autowired
    private RevenueStatsRepository revenueStatsRepository;

    public List<RevenueStats> findAll() {
        return revenueStatsRepository.findAll();
    }

    public Optional<RevenueStats> findById(Long id) {
        return revenueStatsRepository.findById(id);
    }

    public List<RevenueStats> findByAdSlotId(Long adSlotId) {
        return revenueStatsRepository.findByAdSlotId(adSlotId);
    }

    public List<RevenueStats> findByDateRange(LocalDate startDate, LocalDate endDate) {
        return revenueStatsRepository.findAllByDateRange(startDate, endDate);
    }

    public List<RevenueStats> findByAdSlotIdAndDateRange(Long adSlotId, LocalDate startDate, LocalDate endDate) {
        return revenueStatsRepository.findByAdSlotIdAndStatDateBetween(adSlotId, startDate, endDate);
    }

    public Map<String, Object> getSummary(Long adSlotId, LocalDate startDate, LocalDate endDate) {
        List<RevenueStats> stats;
        if (adSlotId != null) {
            stats = findByAdSlotIdAndDateRange(adSlotId, startDate, endDate);
        } else {
            stats = findByDateRange(startDate, endDate);
        }

        long totalImpressions = 0;
        long totalClicks = 0;
        BigDecimal totalRevenue = BigDecimal.ZERO;

        for (RevenueStats stat : stats) {
            totalImpressions += stat.getImpressions();
            totalClicks += stat.getClicks();
            totalRevenue = totalRevenue.add(stat.getRevenue());
        }

        double ctr = totalImpressions > 0 ? (totalClicks * 100.0 / totalImpressions) : 0;
        BigDecimal ecpm = totalImpressions > 0
                ? totalRevenue.multiply(BigDecimal.valueOf(1000)).divide(BigDecimal.valueOf(totalImpressions), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO;

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalImpressions", totalImpressions);
        summary.put("totalClicks", totalClicks);
        summary.put("totalRevenue", totalRevenue);
        summary.put("ctr", ctr);
        summary.put("ecpm", ecpm);
        summary.put("stats", stats);

        return summary;
    }

    public byte[] exportToExcel(Long adSlotId, LocalDate startDate, LocalDate endDate) throws IOException {
        List<RevenueStats> stats;
        if (adSlotId != null) {
            stats = findByAdSlotIdAndDateRange(adSlotId, startDate, endDate);
        } else {
            stats = findByDateRange(startDate, endDate);
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("收益统计");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "广告位ID", "日期", "曝光量", "点击量", "点击率(%)", "收入(元)", "eCPM(元)"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (RevenueStats stat : stats) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stat.getId());
                row.createCell(1).setCellValue(stat.getAdSlotId());
                row.createCell(2).setCellValue(stat.getStatDate().toString());
                row.createCell(3).setCellValue(stat.getImpressions());
                row.createCell(4).setCellValue(stat.getClicks());
                row.createCell(5).setCellValue(String.format("%.2f", stat.getCtr()));
                row.createCell(6).setCellValue(stat.getRevenue().doubleValue());
                row.createCell(7).setCellValue(stat.getEcpm().doubleValue());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }

    public RevenueStats recordImpression(Long adSlotId) {
        LocalDate today = LocalDate.now();
        Optional<RevenueStats> opt = revenueStatsRepository.findByAdSlotIdAndStatDate(adSlotId, today);
        RevenueStats stats;
        if (opt.isPresent()) {
            stats = opt.get();
            stats.setImpressions(stats.getImpressions() + 1);
        } else {
            stats = new RevenueStats();
            stats.setAdSlotId(adSlotId);
            stats.setStatDate(today);
            stats.setImpressions(1L);
        }
        return revenueStatsRepository.save(stats);
    }

    public RevenueStats recordClick(Long adSlotId) {
        LocalDate today = LocalDate.now();
        Optional<RevenueStats> opt = revenueStatsRepository.findByAdSlotIdAndStatDate(adSlotId, today);
        RevenueStats stats;
        if (opt.isPresent()) {
            stats = opt.get();
            stats.setClicks(stats.getClicks() + 1);
            stats.setRevenue(stats.getRevenue().add(BigDecimal.valueOf(0.5)));
        } else {
            stats = new RevenueStats();
            stats.setAdSlotId(adSlotId);
            stats.setStatDate(today);
            stats.setClicks(1L);
            stats.setRevenue(BigDecimal.valueOf(0.5));
        }
        return revenueStatsRepository.save(stats);
    }
}
