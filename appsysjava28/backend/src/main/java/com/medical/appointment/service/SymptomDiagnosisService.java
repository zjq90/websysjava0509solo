package com.medical.appointment.service;

import com.medical.appointment.entity.Department;
import com.medical.appointment.entity.Doctor;
import com.medical.appointment.entity.SymptomKnowledge;
import com.medical.appointment.repository.SymptomKnowledgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SymptomDiagnosisService {

    @Autowired
    private SymptomKnowledgeRepository symptomKnowledgeRepository;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DepartmentService departmentService;

    private static final Map<String, Long[]> SYMPTOM_DEPARTMENT_MAP = new HashMap<>();
    
    static {
        SYMPTOM_DEPARTMENT_MAP.put("头痛", new Long[]{1L, 2L});
        SYMPTOM_DEPARTMENT_MAP.put("头晕", new Long[]{1L, 2L});
        SYMPTOM_DEPARTMENT_MAP.put("发热", new Long[]{3L, 4L});
        SYMPTOM_DEPARTMENT_MAP.put("发烧", new Long[]{3L, 4L});
        SYMPTOM_DEPARTMENT_MAP.put("咳嗽", new Long[]{3L, 4L});
        SYMPTOM_DEPARTMENT_MAP.put("感冒", new Long[]{3L, 4L});
        SYMPTOM_DEPARTMENT_MAP.put("胸闷", new Long[]{5L, 6L});
        SYMPTOM_DEPARTMENT_MAP.put("胸痛", new Long[]{5L, 6L});
        SYMPTOM_DEPARTMENT_MAP.put("胃痛", new Long[]{7L, 8L});
        SYMPTOM_DEPARTMENT_MAP.put("腹痛", new Long[]{7L, 8L});
        SYMPTOM_DEPARTMENT_MAP.put("腹泻", new Long[]{7L, 8L});
        SYMPTOM_DEPARTMENT_MAP.put("呕吐", new Long[]{7L, 8L});
        SYMPTOM_DEPARTMENT_MAP.put("高血压", new Long[]{5L, 1L});
        SYMPTOM_DEPARTMENT_MAP.put("糖尿病", new Long[]{1L, 9L});
        SYMPTOM_DEPARTMENT_MAP.put("皮肤痒", new Long[]{10L});
        SYMPTOM_DEPARTMENT_MAP.put("皮疹", new Long[]{10L});
        SYMPTOM_DEPARTMENT_MAP.put("眼睛痛", new Long[]{11L});
        SYMPTOM_DEPARTMENT_MAP.put("视力模糊", new Long[]{11L});
        SYMPTOM_DEPARTMENT_MAP.put("牙痛", new Long[]{12L});
        SYMPTOM_DEPARTMENT_MAP.put("牙龈出血", new Long[]{12L});
        SYMPTOM_DEPARTMENT_MAP.put("耳朵痛", new Long[]{13L});
        SYMPTOM_DEPARTMENT_MAP.put("听力下降", new Long[]{13L});
        SYMPTOM_DEPARTMENT_MAP.put("鼻塞", new Long[]{13L});
        SYMPTOM_DEPARTMENT_MAP.put("关节痛", new Long[]{14L});
        SYMPTOM_DEPARTMENT_MAP.put("骨折", new Long[]{15L});
        SYMPTOM_DEPARTMENT_MAP.put("外伤", new Long[]{15L});
        SYMPTOM_DEPARTMENT_MAP.put("月经不调", new Long[]{16L});
        SYMPTOM_DEPARTMENT_MAP.put("白带异常", new Long[]{16L});
        SYMPTOM_DEPARTMENT_MAP.put("怀孕", new Long[]{16L});
        SYMPTOM_DEPARTMENT_MAP.put("小儿发热", new Long[]{17L});
        SYMPTOM_DEPARTMENT_MAP.put("小儿咳嗽", new Long[]{17L});
        SYMPTOM_DEPARTMENT_MAP.put("失眠", new Long[]{1L, 18L});
        SYMPTOM_DEPARTMENT_MAP.put("焦虑", new Long[]{18L});
        SYMPTOM_DEPARTMENT_MAP.put("抑郁", new Long[]{18L});
    }

    public Map<String, Object> analyzeSymptoms(String symptomsText) {
        Map<String, Object> result = new HashMap<>();
        
        String[] symptoms = symptomsText.split("[,，、;；\\s]+");
        
        Map<Long, Double> departmentScores = new HashMap<>();
        List<String> matchedSymptoms = new ArrayList<>();
        
        for (String symptom : symptoms) {
            String s = symptom.trim();
            if (s.isEmpty()) continue;
            
            for (Map.Entry<String, Long[]> entry : SYMPTOM_DEPARTMENT_MAP.entrySet()) {
                if (s.contains(entry.getKey()) || entry.getKey().contains(s)) {
                    matchedSymptoms.add(entry.getKey());
                    Long[] deptIds = entry.getValue();
                    for (int i = 0; i < deptIds.length; i++) {
                        double score = 1.0 - (i * 0.2);
                        departmentScores.merge(deptIds[i], score, Double::sum);
                    }
                }
            }
        }

        List<Map<String, Object>> departments = new ArrayList<>();
        List<Map<String, Object>> doctors = new ArrayList<>();

        if (departmentScores.isEmpty()) {
            List<Map<String, Object>> allDepts = departmentService.getDepartmentTree();
            if (!allDepts.isEmpty()) {
                departments = allDepts.stream().limit(3).collect(Collectors.toList());
                if (!departments.isEmpty()) {
                    Long firstDeptId = Long.valueOf(departments.get(0).get("id").toString());
                    doctors = doctorService.getDoctorsByDepartment(firstDeptId).stream().limit(5).collect(Collectors.toList());
                }
            }
        } else {
            List<Map.Entry<Long, Double>> sortedDepts = departmentScores.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .collect(Collectors.toList());
            
            for (Map.Entry<Long, Double> entry : sortedDepts) {
                try {
                    Map<String, Object> deptDetail = departmentService.getDepartmentDetail(entry.getKey());
                    deptDetail.put("matchScore", String.format("%.0f%%", entry.getValue() * 100 / sortedDepts.size()));
                    departments.add(deptDetail);
                    
                    if (doctors.isEmpty()) {
                        doctors = doctorService.getDoctorsByDepartment(entry.getKey()).stream().limit(5).collect(Collectors.toList());
                    }
                } catch (Exception e) {
                }
            }
        }

        result.put("matchedSymptoms", matchedSymptoms.isEmpty() ? Arrays.asList(symptoms) : matchedSymptoms);
        result.put("recommendedDepartments", departments);
        result.put("recommendedDoctors", doctors);
        result.put("analysisText", generateAnalysisText(matchedSymptoms));

        return result;
    }

    private String generateAnalysisText(List<String> symptoms) {
        if (symptoms.isEmpty()) {
            return "根据您的描述，建议您选择合适的科室进行进一步检查。如有疑问，可先进行全科咨询。";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("根据您描述的症状（");
        sb.append(String.join("、", symptoms));
        sb.append("），系统为您推荐以下科室和医生。");
        sb.append("建议您根据自身情况选择合适的医生进行预约挂号。");
        sb.append("如症状严重，请及时就医。");
        
        return sb.toString();
    }

    public List<String> getHotSymptoms() {
        return Arrays.asList(
            "头痛、发热",
            "咳嗽、感冒",
            "胃痛、腹泻",
            "皮肤瘙痒",
            "失眠、焦虑",
            "关节疼痛",
            "月经不调",
            "小儿发热"
        );
    }
}
