package com.pethospital.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pethospital.dto.StatisticsDTO;
import com.pethospital.entity.Disease;
import com.pethospital.entity.Medicine;
import com.pethospital.repository.ConsultationRepository;
import com.pethospital.repository.DiseaseRepository;
import com.pethospital.repository.MedicineRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 统计服务
 * 提供接诊量统计、疾病排行、药品使用分析等功能
 * 
 * @author Pet Hospital Team
 */
@Slf4j
@Service
public class StatisticsService {
    
    @Autowired
    private ConsultationRepository consultationRepository;
    
    @Autowired
    private DiseaseRepository diseaseRepository;
    
    @Autowired
    private MedicineRepository medicineRepository;
    
    /**
     * 获取统计数据
     * 
     * @param days 统计天数，默认30天
     * @return 统计数据
     */
    public StatisticsDTO getStatistics(Integer days) {
        if (days == null || days <= 0) {
            days = 30;
        }
        
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);
        
        StatisticsDTO result = new StatisticsDTO();
        
        // 1. 接诊量统计
        result.setConsultationStats(calculateConsultationStats(startTime, endTime, days));
        
        // 2. 常见疾病排行
        result.setDiseaseRanks(calculateDiseaseRanks(startTime, endTime));
        
        // 3. 药品使用分析
        result.setMedicineUsages(calculateMedicineUsages(startTime, endTime));
        
        return result;
    }
    
    /**
     * 计算接诊量统计
     */
    private StatisticsDTO.ConsultationStats calculateConsultationStats(
            LocalDateTime startTime, LocalDateTime endTime, int days) {
        StatisticsDTO.ConsultationStats stats = new StatisticsDTO.ConsultationStats();
        
        // 总接诊量
        stats.setTotalCount(consultationRepository.count());
        
        // 今日接诊量
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        stats.setTodayCount(consultationRepository.countByDateRange(todayStart, todayEnd));
        
        // 本周接诊量
        LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
        stats.setWeekCount(consultationRepository.countByDateRange(weekStart, endTime));
        
        // 本月接诊量
        LocalDateTime monthStart = LocalDateTime.now().minusMonths(1);
        stats.setMonthCount(consultationRepository.countByDateRange(monthStart, endTime));
        
        // 每日数据（按日期聚合）
        List<Object[]> rawData = consultationRepository.countGroupByDate(startTime, endTime);
        Map<LocalDate, Long> dateCountMap = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Object[] row : rawData) {
            LocalDateTime dateTime = (LocalDateTime) row[0];
            LocalDate date = dateTime.toLocalDate();
            Long count = ((Number) row[1]).longValue();
            dateCountMap.merge(date, count, Long::sum);
        }
        
        List<Map<String, Object>> dailyData = new ArrayList<>();
        for (Map.Entry<LocalDate, Long> entry : dateCountMap.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", entry.getKey().format(dateFormatter));
            map.put("count", entry.getValue());
            dailyData.add(map);
        }
        stats.setDailyData(dailyData);
        
        // 按周汇总数据
        stats.setWeeklyData(aggregateByPeriod(dailyData, 7));
        
        // 按月汇总数据（如果天数超过30天）
        if (days > 30) {
            stats.setMonthlyData(aggregateByPeriod(dailyData, 30));
        }
        
        return stats;
    }
    
    /**
     * 按周期汇总数据
     */
    private List<Map<String, Object>> aggregateByPeriod(List<Map<String, Object>> dailyData, int periodDays) {
        Map<String, Long> periodMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Map<String, Object> row : dailyData) {
            LocalDate date = LocalDate.parse(row.get("date").toString(), formatter);
            Long count = ((Number) row.get("count")).longValue();
            String periodKey;
            
            if (periodDays == 7) {
                // 按周：yyyy年第x周
                int weekOfYear = date.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
                periodKey = date.getYear() + "年第" + weekOfYear + "周";
            } else {
                // 按月：yyyy年x月
                periodKey = date.getYear() + "年" + date.getMonthValue() + "月";
            }
            
            periodMap.merge(periodKey, count, Long::sum);
        }
        
        return periodMap.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("period", entry.getKey());
            map.put("count", entry.getValue());
            return map;
        }).collect(Collectors.toList());
    }
    
    /**
     * 计算疾病排行
     */
    private List<StatisticsDTO.DiseaseRank> calculateDiseaseRanks(
            LocalDateTime startTime, LocalDateTime endTime) {
        List<String> allDiagnosisIds = consultationRepository.findAllDiagnosisIds(startTime, endTime);
        
        // 统计每个疾病出现的次数
        Map<Long, Long> diseaseCountMap = new HashMap<>();
        for (String diagnosisIdsStr : allDiagnosisIds) {
            if (diagnosisIdsStr == null || diagnosisIdsStr.trim().isEmpty()) continue;
            String[] ids = diagnosisIdsStr.split(",");
            for (String idStr : ids) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    diseaseCountMap.merge(id, 1L, Long::sum);
                } catch (NumberFormatException ignored) {}
            }
        }
        
        // 计算总数
        long total = diseaseCountMap.values().stream().mapToLong(Long::longValue).sum();
        
        // 获取所有疾病信息
        List<Disease> allDiseases = diseaseRepository.findByStatus(1);
        Map<Long, Disease> diseaseMap = allDiseases.stream()
                .collect(Collectors.toMap(Disease::getId, d -> d));
        
        // 构建排行列表
        List<StatisticsDTO.DiseaseRank> ranks = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : diseaseCountMap.entrySet()) {
            Disease disease = diseaseMap.get(entry.getKey());
            if (disease != null) {
                StatisticsDTO.DiseaseRank rank = new StatisticsDTO.DiseaseRank();
                rank.setDiseaseId(disease.getId());
                rank.setDiseaseName(disease.getName());
                rank.setCount(entry.getValue());
                rank.setPercentage(total > 0 ? (entry.getValue() * 100.0 / total) : 0);
                rank.setTreatment(disease.getTreatment());
                ranks.add(rank);
            }
        }
        
        // 按出现次数降序排序，取前10个
        return ranks.stream()
                .sorted(Comparator.comparingLong(StatisticsDTO.DiseaseRank::getCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    
    /**
     * 计算药品使用分析
     */
    private List<StatisticsDTO.MedicineUsage> calculateMedicineUsages(
            LocalDateTime startTime, LocalDateTime endTime) {
        List<String> allMedicineIds = consultationRepository.findAllMedicineIds(startTime, endTime);
        
        // 统计每个药品出现的次数
        Map<Long, Long> medicineCountMap = new HashMap<>();
        for (String medicineIdsStr : allMedicineIds) {
            if (medicineIdsStr == null || medicineIdsStr.trim().isEmpty()) continue;
            String[] ids = medicineIdsStr.split(",");
            for (String idStr : ids) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    medicineCountMap.merge(id, 1L, Long::sum);
                } catch (NumberFormatException ignored) {}
            }
        }
        
        // 计算总数
        long total = medicineCountMap.values().stream().mapToLong(Long::longValue).sum();
        
        // 获取所有药品信息
        List<Medicine> allMedicines = medicineRepository.findByStatus(1);
        Map<Long, Medicine> medicineMap = allMedicines.stream()
                .collect(Collectors.toMap(Medicine::getId, m -> m));
        
        // 构建使用分析列表
        List<StatisticsDTO.MedicineUsage> usages = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : medicineCountMap.entrySet()) {
            Medicine medicine = medicineMap.get(entry.getKey());
            if (medicine != null) {
                StatisticsDTO.MedicineUsage usage = new StatisticsDTO.MedicineUsage();
                usage.setMedicineId(medicine.getId());
                usage.setMedicineName(medicine.getName());
                usage.setCategory(medicine.getCategory());
                usage.setUsageCount(entry.getValue());
                usage.setPercentage(total > 0 ? (entry.getValue() * 100.0 / total) : 0);
                usages.add(usage);
            }
        }
        
        // 按使用次数降序排序，取前10个
        return usages.stream()
                .sorted(Comparator.comparingLong(StatisticsDTO.MedicineUsage::getUsageCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    
    /**
     * 导出统计数据到Excel
     * 
     * @param response HTTP响应
     * @param days 统计天数
     * @throws IOException IO异常
     */
    public void exportStatistics(HttpServletResponse response, Integer days) throws IOException {
        StatisticsDTO statistics = getStatistics(days);
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("宠物医院统计数据", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        
        // 准备导出数据
        List<ExcelExportVO> exportData = new ArrayList<>();
        
        // 添加接诊量每日数据
        for (Map<String, Object> daily : statistics.getConsultationStats().getDailyData()) {
            ExcelExportVO vo = new ExcelExportVO();
            vo.setType("接诊量统计");
            vo.setItemName(daily.get("date").toString());
            vo.setItemValue(daily.get("count").toString());
            vo.setRemark("每日接诊量");
            exportData.add(vo);
        }
        
        // 添加疾病排行数据
        for (StatisticsDTO.DiseaseRank rank : statistics.getDiseaseRanks()) {
            ExcelExportVO vo = new ExcelExportVO();
            vo.setType("常见疾病排行");
            vo.setItemName(rank.getDiseaseName());
            vo.setItemValue(rank.getCount().toString());
            vo.setRemark(String.format("占比%.2f%%", rank.getPercentage()));
            exportData.add(vo);
        }
        
        // 添加药品使用分析数据
        for (StatisticsDTO.MedicineUsage usage : statistics.getMedicineUsages()) {
            ExcelExportVO vo = new ExcelExportVO();
            vo.setType("药品使用分析");
            vo.setItemName(usage.getMedicineName() + "(" + usage.getCategory() + ")");
            vo.setItemValue(usage.getUsageCount().toString());
            vo.setRemark(String.format("占比%.2f%%", usage.getPercentage()));
            exportData.add(vo);
        }
        
        EasyExcel.write(response.getOutputStream(), ExcelExportVO.class)
                .sheet("统计数据")
                .doWrite(exportData);
    }
    
    /**
     * Excel导出VO
     */
    @Data
    public static class ExcelExportVO {
        @ExcelProperty("统计类型")
        private String type;
        
        @ExcelProperty("项目名称")
        private String itemName;
        
        @ExcelProperty("数值")
        private String itemValue;
        
        @ExcelProperty("备注")
        private String remark;
    }
}
