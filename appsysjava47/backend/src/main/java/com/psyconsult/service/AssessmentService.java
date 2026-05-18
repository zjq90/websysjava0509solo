package com.psyconsult.service;

import com.psyconsult.entity.Assessment;
import com.psyconsult.repository.AssessmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public AssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Transactional
    public Assessment createAssessment(Long userId, Assessment assessment) {
        assessment.setUserId(userId);
        
        List<String> recommendations = generateRecommendations(assessment);
        assessment.setRecommendations(recommendations);
        
        assessment.setSummary(generateSummary(assessment));
        
        return assessmentRepository.save(assessment);
    }

    public List<Assessment> getUserAssessments(Long userId) {
        return assessmentRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }

    public Assessment getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("测评记录不存在"));
    }

    private List<String> generateRecommendations(Assessment assessment) {
        List<String> recommendations = new ArrayList<>();
        
        Integer phq9Score = assessment.getPhq9Score();
        if (phq9Score != null) {
            if (phq9Score >= 20) {
                recommendations.add("您的抑郁得分较高，建议立即寻求专业心理咨询师的帮助");
                recommendations.add("建议每天进行30分钟的有氧运动");
            } else if (phq9Score >= 10) {
                recommendations.add("建议进行定期的心理咨询");
                recommendations.add("尝试正念冥想练习");
            } else if (phq9Score >= 5) {
                recommendations.add("保持规律的作息时间");
                recommendations.add("多与家人朋友交流");
            }
        }
        
        Integer gad7Score = assessment.getGad7Score();
        if (gad7Score != null) {
            if (gad7Score >= 15) {
                recommendations.add("您的焦虑得分较高，建议寻求专业帮助");
                recommendations.add("学习深呼吸放松技巧");
            } else if (gad7Score >= 10) {
                recommendations.add("建议进行焦虑管理的心理咨询");
                recommendations.add("尝试渐进式肌肉放松");
            } else if (gad7Score >= 5) {
                recommendations.add("保持适度的运动");
                recommendations.add("避免过度咖啡因摄入");
            }
        }
        
        if (recommendations.isEmpty()) {
            recommendations.add("您的心理状态良好，请继续保持健康的生活方式");
            recommendations.add("定期进行自我心理评估");
        }
        
        return recommendations;
    }

    private String generateSummary(Assessment assessment) {
        StringBuilder summary = new StringBuilder();
        
        if (assessment.getType().equals("PHQ9")) {
            int score = assessment.getPhq9Score() != null ? assessment.getPhq9Score() : 0;
            summary.append("抑郁自评量表(PHQ-9)得分: ").append(score).append("。");
            if (score >= 20) {
                summary.append("重度抑郁症状，请务必及时就医。");
            } else if (score >= 15) {
                summary.append("中度重度抑郁症状，建议咨询专业人士。");
            } else if (score >= 10) {
                summary.append("中度抑郁症状，建议关注心理健康。");
            } else if (score >= 5) {
                summary.append("轻度抑郁症状，注意自我调节。");
            } else {
                summary.append("无明显抑郁症状。");
            }
        } else if (assessment.getType().equals("GAD7")) {
            int score = assessment.getGad7Score() != null ? assessment.getGad7Score() : 0;
            summary.append("焦虑自评量表(GAD-7)得分: ").append(score).append("。");
            if (score >= 15) {
                summary.append("重度焦虑症状，请务必及时就医。");
            } else if (score >= 10) {
                summary.append("中度焦虑症状，建议咨询专业人士。");
            } else if (score >= 5) {
                summary.append("轻度焦虑症状，注意自我调节。");
            } else {
                summary.append("无明显焦虑症状。");
            }
        }
        
        return summary.toString();
    }
}
