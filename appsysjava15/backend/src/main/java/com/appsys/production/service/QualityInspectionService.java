package com.appsys.production.service;

import com.appsys.production.dto.QualityInspectionDTO;
import com.appsys.production.entity.BatchStage;
import com.appsys.production.entity.QualityInspection;
import com.appsys.production.exception.BusinessException;
import com.appsys.production.repository.BatchStageRepository;
import com.appsys.production.repository.QualityInspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class QualityInspectionService {

    @Autowired
    private QualityInspectionRepository inspectionRepository;

    @Autowired
    private BatchStageRepository stageRepository;

    public List<QualityInspection> getByBatchId(Long batchId) {
        return inspectionRepository.findByBatchId(batchId);
    }

    public QualityInspection getByBatchIdAndStageCode(Long batchId, String stageCode) {
        return inspectionRepository.findByBatchIdAndStageCode(batchId, stageCode).orElse(null);
    }

    @Transactional
    public QualityInspection submit(QualityInspectionDTO dto) {
        validateInspectionData(dto);

        BatchStage stage = stageRepository.findByBatchIdAndStageCode(dto.getBatchId(), dto.getStageCode()).orElseThrow(() -> new BusinessException("环节不存在"));

        if (!"IN_PROGRESS".equals(stage.getStatus())) {
            throw new BusinessException("请先开始当前环节");
        }

        QualityInspection inspection = new QualityInspection();
        inspection.setBatchId(dto.getBatchId());
        inspection.setStageCode(dto.getStageCode());
        inspection.setOperatorId(dto.getOperatorId());
        inspection.setOperatorName(dto.getOperatorName());
        inspection.setMoisture(dto.getMoisture());
        inspection.setPurity(dto.getPurity());
        inspection.setGerminationRate(dto.getGerminationRate());
        inspection.setRemark(dto.getRemark());

        String result = determineResult(dto);
        inspection.setResult(result);

        return inspectionRepository.save(inspection);
    }

    private void validateInspectionData(QualityInspectionDTO dto) {
        if (dto.getMoisture() != null) {
            if (dto.getMoisture().compareTo(BigDecimal.ZERO) < 0 || dto.getMoisture().compareTo(new BigDecimal("100")) > 0) {
                throw new BusinessException("水分范围必须在0-100之间");
            }
        }
        if (dto.getPurity() != null) {
            if (dto.getPurity().compareTo(BigDecimal.ZERO) < 0 || dto.getPurity().compareTo(new BigDecimal("100")) > 0) {
                throw new BusinessException("净度范围必须在0-100之间");
            }
        }
        if (dto.getGerminationRate() != null) {
            if (dto.getGerminationRate().compareTo(BigDecimal.ZERO) < 0 || dto.getGerminationRate().compareTo(new BigDecimal("100")) > 0) {
                throw new BusinessException("发芽率范围必须在0-100之间");
            }
        }
    }

    private String determineResult(QualityInspectionDTO dto) {
        boolean pass = true;
        if (dto.getGerminationRate() != null && dto.getGerminationRate().compareTo(new BigDecimal("85")) < 0) {
            pass = false;
        }
        if (dto.getMoisture() != null && dto.getMoisture().compareTo(new BigDecimal("13")) > 0) {
            pass = false;
        }
        if (dto.getPurity() != null && dto.getPurity().compareTo(new BigDecimal("98")) < 0) {
            pass = false;
        }
        return pass ? "PASS" : "FAIL";
    }
}
