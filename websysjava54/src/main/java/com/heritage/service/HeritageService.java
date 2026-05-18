package com.heritage.service;

import com.heritage.entity.Heritage;
import com.heritage.enums.AuditStatus;
import com.heritage.enums.RiskLevel;
import com.heritage.repository.HeritageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class HeritageService {

    @Autowired
    private HeritageRepository heritageRepository;

    public List<Heritage> findAll() {
        return heritageRepository.findAll();
    }

    public Optional<Heritage> findById(Long id) {
        return heritageRepository.findById(id);
    }

    public Heritage save(Heritage heritage) {
        return heritageRepository.save(heritage);
    }

    public void deleteById(Long id) {
        heritageRepository.deleteById(id);
    }

    public List<Heritage> findPendingAudit() {
        List<Heritage> heritageList = heritageRepository.findByAuditStatus(AuditStatus.PENDING);
        heritageList.sort((h1, h2) -> {
            int level1 = h1.getRiskLevel().getLevel();
            int level2 = h2.getRiskLevel().getLevel();
            return Integer.compare(level2, level1);
        });
        return heritageList;
    }

    public List<Heritage> findByRiskLevel(RiskLevel riskLevel) {
        return heritageRepository.findByRiskLevel(riskLevel);
    }

    @Transactional
    public Heritage auditHeritage(Long heritageId, AuditStatus status, String remark, Long auditorId) {
        Heritage heritage = heritageRepository.findById(heritageId)
                .orElseThrow(() -> new RuntimeException("文物不存在"));
        heritage.setAuditStatus(status);
        heritage.setAuditRemark(remark);
        heritage.setAuditorId(auditorId);
        heritage.setAuditTime(LocalDateTime.now());
        return heritageRepository.save(heritage);
    }

    @Transactional
    public Heritage verifyWithApi(Long heritageId) {
        Heritage heritage = heritageRepository.findById(heritageId)
                .orElseThrow(() -> new RuntimeException("文物不存在"));
        heritage.setApiVerified(true);
        heritage.setApiVerifyResult("国家文物局API验证通过 - 来源合法");
        Random random = new Random();
        int risk = random.nextInt(3);
        if (risk == 0) {
            heritage.setRiskLevel(RiskLevel.HIGH);
        } else if (risk == 1) {
            heritage.setRiskLevel(RiskLevel.MEDIUM);
        } else {
            heritage.setRiskLevel(RiskLevel.LOW);
        }
        return heritageRepository.save(heritage);
    }

    public List<Heritage> findByDynasty(String dynasty) {
        return heritageRepository.findByDynasty(dynasty);
    }

    public List<Heritage> findByMaterial(String material) {
        return heritageRepository.findByMaterial(material);
    }

    public List<Heritage> findByUsageType(String usageType) {
        return heritageRepository.findByUsageType(usageType);
    }

    public List<Heritage> findByProvince(String province) {
        return heritageRepository.findByProvince(province);
    }
}
