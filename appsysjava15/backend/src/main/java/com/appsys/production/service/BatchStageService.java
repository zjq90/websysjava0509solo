package com.appsys.production.service;

import com.appsys.production.dto.StageOperationDTO;
import com.appsys.production.entity.AlertNotification;
import com.appsys.production.entity.BatchStage;
import com.appsys.production.entity.ProductionBatch;
import com.appsys.production.entity.User;
import com.appsys.production.exception.BusinessException;
import com.appsys.production.repository.AlertNotificationRepository;
import com.appsys.production.repository.BatchStageRepository;
import com.appsys.production.repository.ProductionBatchRepository;
import com.appsys.production.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class BatchStageService {

    @Autowired
    private BatchStageRepository stageRepository;

    @Autowired
    private ProductionBatchRepository batchRepository;

    @Autowired
    private AlertNotificationRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    private static final List<String> STAGES = Arrays.asList("CLEANING", "COATING", "PACKAGING", "INSPECTION");

    public List<BatchStage> getByBatchId(Long batchId) {
        return stageRepository.findByBatchIdOrderBySortOrderAsc(batchId);
    }

    public BatchStage getByBatchIdAndStageCode(Long batchId, String stageCode) {
        return stageRepository.findByBatchIdAndStageCode(batchId, stageCode).orElseThrow(() -> new BusinessException("环节不存在"));
    }

    @Transactional
    public BatchStage startStage(StageOperationDTO dto) {
        ProductionBatch batch = batchRepository.findById(dto.getBatchId()).orElseThrow(() -> new BusinessException("批次不存在"));
        BatchStage stage = stageRepository.findByBatchIdAndStageCode(dto.getBatchId(), dto.getStageCode()).orElseThrow(() -> new BusinessException("环节不存在"));

        if (!canStartStage(batch, stage)) {
            throw new BusinessException("当前环节不可开始，请确保上一环节已完成");
        }

        if (!"PENDING".equals(stage.getStatus())) {
            throw new BusinessException("该环节状态不允许开始");
        }

        stage.setStatus("IN_PROGRESS");
        stage.setStartTime(LocalDateTime.now());
        stage.setOperatorId(dto.getOperatorId());
        stage.setOperatorName(dto.getOperatorName());
        stage.setProcessParams(dto.getProcessParams());
        stage.setRemark(dto.getRemark());

        batch.setStatus("IN_PROGRESS");
        batch.setCurrentStage(dto.getStageCode());
        batchRepository.save(batch);

        return stageRepository.save(stage);
    }

    @Transactional
    public BatchStage completeStage(StageOperationDTO dto) {
        BatchStage stage = stageRepository.findByBatchIdAndStageCode(dto.getBatchId(), dto.getStageCode()).orElseThrow(() -> new BusinessException("环节不存在"));

        if (!"IN_PROGRESS".equals(stage.getStatus())) {
            throw new BusinessException("该环节状态不允许完成");
        }

        stage.setStatus("COMPLETED");
        stage.setEndTime(LocalDateTime.now());
        if (dto.getRemark() != null) {
            stage.setRemark(dto.getRemark());
        }

        ProductionBatch batch = batchRepository.findById(dto.getBatchId()).orElseThrow(() -> new BusinessException("批次不存在"));
        int currentIndex = STAGES.indexOf(dto.getStageCode());
        if (currentIndex < STAGES.size() - 1) {
            batch.setCurrentStage(STAGES.get(currentIndex + 1));
        } else {
            batch.setStatus("COMPLETED");
            batch.setCurrentStage(null);
        }
        batchRepository.save(batch);

        return stageRepository.save(stage);
    }

    public void checkTimeoutAndAlert() {
        List<BatchStage> inProgressStages = stageRepository.findByStatusAndEndTimeIsNull("IN_PROGRESS");
        for (BatchStage stage : inProgressStages) {
            if (stage.getStartTime() != null) {
                LocalDateTime now = LocalDateTime.now();
                long hours = java.time.Duration.between(stage.getStartTime(), now).toHours();
                if (hours >= 24) {
                    createAlert(stage);
                }
            }
        }
    }

    private void createAlert(BatchStage stage) {
        List<User> supervisors = userRepository.findByRole("SUPERVISOR");
        for (User supervisor : supervisors) {
            AlertNotification alert = new AlertNotification();
            alert.setBatchId(stage.getBatchId());
            alert.setStageCode(stage.getStageCode());
            alert.setStageName(stage.getStageName());
            alert.setAlertType("TIMEOUT");
            alert.setMessage("生产环节超时预警：" + stage.getStageName() + "环节已超时24小时未完成，请及时处理");
            alert.setSupervisorId(supervisor.getId());
            alert.setSupervisorName(supervisor.getRealName());
            alert.setStatus("UNREAD");
            alertRepository.save(alert);
        }
    }

    private boolean canStartStage(ProductionBatch batch, BatchStage currentStage) {
        if (STAGES.get(0).equals(currentStage.getStageCode())) {
            return true;
        }
        int currentIndex = STAGES.indexOf(currentStage.getStageCode());
        if (currentIndex <= 0) {
            return false;
        }
        String prevStageCode = STAGES.get(currentIndex - 1);
        BatchStage prevStage = stageRepository.findByBatchIdAndStageCode(batch.getId(), prevStageCode).orElse(null);
        return prevStage != null && "COMPLETED".equals(prevStage.getStatus());
    }
}
