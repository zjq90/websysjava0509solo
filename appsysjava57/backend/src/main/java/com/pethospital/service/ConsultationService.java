package com.pethospital.service;

import com.pethospital.entity.Consultation;
import com.pethospital.entity.Hospital;
import com.pethospital.repository.ConsultationRepository;
import com.pethospital.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 在线问诊Service
 * 提供图文问诊、视频问诊、AI紧急症状识别等功能
 * 
 * @author Pet Hospital Team
 */
@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    private static final Set<String> EMERGENCY_SYMPTOMS = new HashSet<>(Arrays.asList(
            "呕吐", "抽搐", "痉挛", "呼吸困难", "窒息", "昏迷", "休克",
            "大量出血", "无法站立", "严重外伤", "中毒", "误食", "高热",
            "急性腹痛", "瘫痪", "癫痫", "心力衰竭"
    ));

    /**
     * AI紧急症状识别
     */
    public Map<String, Object> checkEmergencySymptoms(String symptoms) {
        Map<String, Object> result = new HashMap<>();
        boolean isEmergency = false;
        List<String> matchedSymptoms = new ArrayList<>();
        StringBuilder suggestion = new StringBuilder();

        for (String emergency : EMERGENCY_SYMPTOMS) {
            if (symptoms.contains(emergency)) {
                isEmergency = true;
                matchedSymptoms.add(emergency);
            }
        }

        result.put("isEmergency", isEmergency);
        result.put("matchedSymptoms", matchedSymptoms);

        if (isEmergency) {
            suggestion.append("检测到紧急症状：").append(String.join("、", matchedSymptoms))
                    .append("。建议立即就医！");
            
            List<Hospital> nearbyHospitals = hospitalRepository.findByIs24HoursTrueAndEmergencyServiceTrueAndEnabledTrue();
            result.put("nearby24HourHospitals", nearbyHospitals);
            result.put("suggestion", suggestion.toString());
        } else {
            result.put("suggestion", "症状暂无紧急风险，建议进行在线问诊咨询医生。");
        }

        return result;
    }

    /**
     * 创建问诊
     */
    @Transactional
    public Consultation createConsultation(Consultation consultation) {
        Map<String, Object> emergencyCheck = checkEmergencySymptoms(consultation.getSymptoms());
        consultation.setEmergencyFlag((Boolean) emergencyCheck.get("isEmergency"));
        consultation.setEmergencySuggestion((String) emergencyCheck.get("suggestion"));
        return consultationRepository.save(consultation);
    }

    /**
     * 获取问诊详情
     */
    public Consultation getConsultationById(Long consultationId) {
        return consultationRepository.findById(consultationId).orElse(null);
    }

    /**
     * 获取用户问诊列表
     */
    public List<Consultation> getUserConsultations(Long userId) {
        return consultationRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    /**
     * 获取症状类型列表（分步引导）
     */
    public List<Map<String, Object>> getSymptomTypes() {
        List<Map<String, Object>> types = new ArrayList<>();
        
        Map<String, Object> type1 = new HashMap<>();
        type1.put("id", "skin");
        type1.put("name", "皮肤问题");
        type1.put("icon", "skin");
        type1.put("symptoms", Arrays.asList("脱毛", "皮屑", "红肿", "瘙痒", "皮疹", "溃疡"));
        types.add(type1);

        Map<String, Object> type2 = new HashMap<>();
        type2.put("id", "digestive");
        type2.put("name", "消化问题");
        type2.put("icon", "stomach");
        type2.put("symptoms", Arrays.asList("呕吐", "腹泻", "便秘", "食欲不振", "腹痛", "腹胀"));
        types.add(type2);

        Map<String, Object> type3 = new HashMap<>();
        type3.put("id", "respiratory");
        type3.put("name", "呼吸问题");
        type3.put("icon", "lung");
        type3.put("symptoms", Arrays.asList("咳嗽", "打喷嚏", "流鼻涕", "呼吸困难", "气喘"));
        types.add(type3);

        Map<String, Object> type4 = new HashMap<>();
        type4.put("id", "behavioral");
        type4.put("name", "行为异常");
        type4.put("icon", "brain");
        type4.put("symptoms", Arrays.asList("抽搐", "痉挛", "精神萎靡", "过度兴奋", "攻击性"));
        types.add(type4);

        Map<String, Object> type5 = new HashMap<>();
        type5.put("id", "other");
        type5.put("name", "其他问题");
        type5.put("icon", "help");
        type5.put("symptoms", Arrays.asList("发热", "疼痛", "外伤", "中毒", "其他"));
        types.add(type5);

        return types;
    }
}
