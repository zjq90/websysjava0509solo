package com.psyconsult.service;

import com.psyconsult.entity.Assessment;
import com.psyconsult.entity.Counselor;
import com.psyconsult.repository.AssessmentRepository;
import com.psyconsult.repository.CounselorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CounselorService {

    private final CounselorRepository counselorRepository;
    private final AssessmentRepository assessmentRepository;

    public CounselorService(CounselorRepository counselorRepository,
                            AssessmentRepository assessmentRepository) {
        this.counselorRepository = counselorRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public List<Counselor> getAllCounselors() {
        return counselorRepository.findByIsAvailableTrueAndEnabledTrue();
    }

    public List<Counselor> getCounselorsByFilters(String specialty, String language, 
                                                   BigDecimal minPrice, BigDecimal maxPrice) {
        return counselorRepository.findByFilters(specialty, language, minPrice, maxPrice);
    }

    public List<Counselor> getRecommendedCounselors(Long userId) {
        List<Assessment> assessments = assessmentRepository.findByUserIdOrderByCreateTimeDesc(userId);
        
        List<Counselor> allCounselors = counselorRepository.findByIsAvailableTrueAndEnabledTrue();
        
        if (assessments.isEmpty()) {
            return allCounselors.stream()
                    .sorted(Comparator.comparing(Counselor::getRating).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        }

        Assessment latestAssessment = assessments.get(0);
        List<String> recommendedSpecialties = getSpecialtiesFromAssessment(latestAssessment);

        return allCounselors.stream()
                .sorted((c1, c2) -> {
                    int score1 = calculateMatchScore(c1, recommendedSpecialties, latestAssessment);
                    int score2 = calculateMatchScore(c2, recommendedSpecialties, latestAssessment);
                    return Integer.compare(score2, score1);
                })
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<String> getSpecialtiesFromAssessment(Assessment assessment) {
        List<String> specialties = new ArrayList<>();
        
        if (assessment.getPhq9Score() != null && assessment.getPhq9Score() >= 10) {
            specialties.add("抑郁");
            specialties.add("情绪管理");
        }
        if (assessment.getGad7Score() != null && assessment.getGad7Score() >= 10) {
            specialties.add("焦虑");
            specialties.add("情绪管理");
        }
        
        return specialties;
    }

    private int calculateMatchScore(Counselor counselor, List<String> recommendedSpecialties, Assessment assessment) {
        int score = 0;
        
        if (counselor.getSpecialties() != null) {
            for (String specialty : recommendedSpecialties) {
                if (counselor.getSpecialties().contains(specialty)) {
                    score += 30;
                }
            }
        }
        
        if (counselor.getRating() != null) {
            score += counselor.getRating().intValue() * 8;
        }
        
        if (counselor.getExperienceYears() != null) {
            score += Math.min(counselor.getExperienceYears(), 10) * 2;
        }
        
        return score;
    }

    public Counselor getCounselorById(Long id) {
        return counselorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("咨询师不存在"));
    }
}
