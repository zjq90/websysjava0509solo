package com.petclinic.service;

import com.petclinic.dto.DashboardStatsDTO;
import com.petclinic.dto.DiseaseHeatmapDTO;
import com.petclinic.entity.Consultation;
import com.petclinic.entity.DiseaseRecord;
import com.petclinic.entity.PetOwner;
import com.petclinic.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计分析Service
 */
@Service
public class StatisticsService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private DiseaseRecordRepository diseaseRecordRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    /**
     * 获取首页统计数据
     */
    public DashboardStatsDTO getDashboardStats() {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setTodayConsultations(consultationRepository.countByDateRange(todayStart, todayEnd));
        stats.setTodayRevenue(consultationRepository.sumFeeByDateRange(todayStart, todayEnd));
        stats.setTodayEmergency(consultationRepository.countEmergencyByDateRange(todayStart, todayEnd));
        stats.setPendingDoctorAudit((long) doctorRepository.findByAuditStatus(0).size());
        stats.setPendingOwnerAudit((long) petOwnerRepository.findByRealNameStatus(1).size());
        stats.setWarningMedicineCount((long) medicineRepository.findWarningMedicines().size());

        return stats;
    }

    /**
     * 获取疾病热力图数据
     */
    public List<DiseaseHeatmapDTO> getDiseaseHeatmap(LocalDateTime startTime, LocalDateTime endTime, 
                                                      String petType, Integer minAge, Integer maxAge) {
        List<DiseaseRecord> records;
        if (petType != null && minAge != null && maxAge != null) {
            records = diseaseRecordRepository.findByPetTypeAndPetAgeBetween(petType, minAge, maxAge);
        } else if (startTime != null && endTime != null) {
            records = diseaseRecordRepository.findByRecordTimeBetween(startTime, endTime);
        } else {
            records = diseaseRecordRepository.findAll();
        }

        // 按疾病名称、宠物类型、年龄分组统计
        Map<String, Map<Integer, Map<String, Long>>> groupedData = new HashMap<>();
        for (DiseaseRecord record : records) {
            groupedData
                .computeIfAbsent(record.getDiseaseName(), k -> new HashMap<>())
                .computeIfAbsent(record.getPetAge(), k -> new HashMap<>())
                .merge(record.getPetType(), 1L, Long::sum);
        }

        List<DiseaseHeatmapDTO> result = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Map<String, Long>>> diseaseEntry : groupedData.entrySet()) {
            for (Map.Entry<Integer, Map<String, Long>> ageEntry : diseaseEntry.getValue().entrySet()) {
                for (Map.Entry<String, Long> typeEntry : ageEntry.getValue().entrySet()) {
                    DiseaseHeatmapDTO dto = new DiseaseHeatmapDTO();
                    dto.setDiseaseName(diseaseEntry.getKey());
                    dto.setPetAge(ageEntry.getKey());
                    dto.setPetType(typeEntry.getKey());
                    dto.setCount(typeEntry.getValue());
                    result.add(dto);
                }
            }
        }

        return result;
    }

    /**
     * 获取用户增长数据
     */
    public Map<String, Object> getUserGrowthData(LocalDateTime startTime, LocalDateTime endTime) {
        List<PetOwner> owners = petOwnerRepository.findByCreateTimeBetween(startTime, endTime);
        
        // 按日期分组统计
        Map<LocalDate, Long> dailyGrowth = owners.stream()
            .collect(Collectors.groupingBy(
                owner -> owner.getCreateTime().toLocalDate(),
                Collectors.counting()
            ));

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", owners.size());
        result.put("dailyData", dailyGrowth);
        return result;
    }

    /**
     * 获取医院运营数据
     */
    public Map<String, Object> getHospitalOperationData(LocalDateTime startTime, LocalDateTime endTime) {
        List<Consultation> consultations = consultationRepository.findByCreateTimeBetween(startTime, endTime);

        Map<LocalDate, Long> dailyConsultations = consultations.stream()
            .collect(Collectors.groupingBy(
                c -> c.getCreateTime().toLocalDate(),
                Collectors.counting()
            ));

        double totalRevenue = consultations.stream()
            .filter(c -> c.getStatus() == 2 && c.getFee() != null)
            .mapToDouble(c -> c.getFee().doubleValue())
            .sum();

        long emergencyCount = consultations.stream()
            .filter(c -> c.getIsEmergency() == 1)
            .count();

        Map<String, Object> result = new HashMap<>();
        result.put("totalConsultations", consultations.size());
        result.put("totalRevenue", totalRevenue);
        result.put("emergencyCount", emergencyCount);
        result.put("dailyConsultations", dailyConsultations);
        return result;
    }

    /**
     * 导出问诊数据（脱敏）
     */
    public byte[] exportConsultationData(LocalDateTime startTime, LocalDateTime endTime) throws Exception {
        List<Consultation> consultations = consultationRepository.findByCreateTimeBetween(startTime, endTime);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("问诊记录");

        // 创建表头
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("问诊单号");
        headerRow.createCell(1).setCellValue("日期");
        headerRow.createCell(2).setCellValue("科室");
        headerRow.createCell(3).setCellValue("宠物类型");
        headerRow.createCell(4).setCellValue("宠物年龄");
        headerRow.createCell(5).setCellValue("是否急诊");
        headerRow.createCell(6).setCellValue("费用");
        headerRow.createCell(7).setCellValue("状态");

        // 填充数据（脱敏处理）
        int rowNum = 1;
        for (Consultation consultation : consultations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(maskString(consultation.getConsultationNo()));
            row.createCell(1).setCellValue(consultation.getCreateTime().toLocalDate().toString());
            row.createCell(2).setCellValue(consultation.getDepartmentId().toString());
            row.createCell(3).setCellValue(consultation.getPetType() != null ? consultation.getPetType() : "");
            row.createCell(4).setCellValue(consultation.getPetAge() != null ? consultation.getPetAge() : 0);
            row.createCell(5).setCellValue(consultation.getIsEmergency() == 1 ? "是" : "否");
            row.createCell(6).setCellValue(consultation.getFee() != null ? consultation.getFee().doubleValue() : 0);
            row.createCell(7).setCellValue(getStatusText(consultation.getStatus()));
        }

        // 自动调整列宽
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }

    /**
     * 脱敏处理字符串
     */
    private String maskString(String str) {
        if (str == null || str.length() <= 4) {
            return "****";
        }
        return str.substring(0, 2) + "****" + str.substring(str.length() - 2);
    }

    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        switch (status) {
            case 0: return "待接诊";
            case 1: return "问诊中";
            case 2: return "已完成";
            case 3: return "已取消";
            default: return "未知";
        }
    }
}
