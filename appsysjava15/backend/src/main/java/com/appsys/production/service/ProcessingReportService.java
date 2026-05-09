package com.appsys.production.service;

import com.appsys.production.entity.*;
import com.appsys.production.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ProcessingReportService {

    @Autowired
    private ProcessingReportRepository reportRepository;

    @Autowired
    private ProductionBatchRepository batchRepository;

    @Autowired
    private BatchStageRepository stageRepository;

    @Autowired
    private QualityInspectionRepository inspectionRepository;

    @Autowired
    private UserRepository userRepository;

    public ProcessingReport getByBatchId(Long batchId) {
        return reportRepository.findByBatchId(batchId).orElse(null);
    }

    public ProcessingReport getByBatchNo(String batchNo) {
        return reportRepository.findByBatchNo(batchNo).orElse(null);
    }

    @Transactional
    public ProcessingReport generateReport(Long batchId, Long userId) {
        ProductionBatch batch = batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("批次不存在"));
        List<BatchStage> stages = stageRepository.findByBatchIdOrderBySortOrderAsc(batchId);
        List<QualityInspection> inspections = inspectionRepository.findByBatchId(batchId);
        User user = userRepository.findById(userId).orElse(null);

        ProcessingReport existingReport = reportRepository.findByBatchId(batchId).orElse(null);
        if (existingReport != null) {
            reportRepository.delete(existingReport);
        }

        ProcessingReport report = new ProcessingReport();
        report.setBatchId(batchId);
        report.setBatchNo(batch.getBatchNo());
        report.setReportNo("RPT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        report.setGeneratedBy(userId);
        report.setGeneratedByName(user != null ? user.getRealName() : "系统");

        StringBuilder content = new StringBuilder();
        content.append("========================================\n");
        content.append("           生产加工报告\n");
        content.append("========================================\n\n");
        content.append("【基本信息】\n");
        content.append("批次编号：").append(batch.getBatchNo()).append("\n");
        content.append("产品名称：").append(batch.getProductName()).append("\n");
        content.append("生产数量：").append(batch.getQuantity()).append(" ").append(batch.getUnit()).append("\n");
        content.append("保质期：").append(batch.getShelfLife()).append("\n");
        if (batch.getCustomerName() != null) {
            content.append("客户名称：").append(batch.getCustomerName()).append("\n");
        }
        content.append("批次状态：").append(getStatusText(batch.getStatus())).append("\n\n");

        content.append("【生产环节记录】\n");
        for (BatchStage stage : stages) {
            content.append("\n环节：").append(stage.getStageName()).append("\n");
            content.append("状态：").append(getStageStatusText(stage.getStatus())).append("\n");
            if (stage.getStartTime() != null) {
                content.append("开始时间：").append(stage.getStartTime()).append("\n");
            }
            if (stage.getEndTime() != null) {
                content.append("结束时间：").append(stage.getEndTime()).append("\n");
            }
            if (stage.getOperatorName() != null) {
                content.append("操作人：").append(stage.getOperatorName()).append("\n");
            }
            if (stage.getProcessParams() != null) {
                content.append("工艺参数：").append(stage.getProcessParams()).append("\n");
            }
        }

        if (!inspections.isEmpty()) {
            content.append("\n\n【质检记录】\n");
            for (QualityInspection inspection : inspections) {
                content.append("\n质检环节：").append(getStageName(inspection.getStageCode())).append("\n");
                if (inspection.getMoisture() != null) {
                    content.append("水分：").append(inspection.getMoisture()).append("%\n");
                }
                if (inspection.getPurity() != null) {
                    content.append("净度：").append(inspection.getPurity()).append("%\n");
                }
                if (inspection.getGerminationRate() != null) {
                    content.append("发芽率：").append(inspection.getGerminationRate()).append("%\n");
                }
                content.append("质检结果：").append(getInspectionResultText(inspection.getResult())).append("\n");
                if (inspection.getOperatorName() != null) {
                    content.append("质检人：").append(inspection.getOperatorName()).append("\n");
                }
            }
        }

        content.append("\n========================================\n");
        content.append("报告生成时间：").append(LocalDateTime.now()).append("\n");
        content.append("========================================");

        report.setContent(content.toString());
        return reportRepository.save(report);
    }

    private String getStatusText(String status) {
        if ("PENDING".equals(status)) {
            return "待开始";
        } else if ("IN_PROGRESS".equals(status)) {
            return "进行中";
        } else if ("COMPLETED".equals(status)) {
            return "已完成";
        }
        return status;
    }

    private String getStageStatusText(String status) {
        if ("PENDING".equals(status)) {
            return "待开始";
        } else if ("IN_PROGRESS".equals(status)) {
            return "进行中";
        } else if ("COMPLETED".equals(status)) {
            return "已完成";
        }
        return status;
    }

    private String getInspectionResultText(String result) {
        return "PASS".equals(result) ? "合格" : "不合格";
    }

    private String getStageName(String stageCode) {
        if ("CLEANING".equals(stageCode)) {
            return "清选";
        } else if ("COATING".equals(stageCode)) {
            return "包衣";
        } else if ("PACKAGING".equals(stageCode)) {
            return "分装";
        } else if ("INSPECTION".equals(stageCode)) {
            return "质检";
        }
        return stageCode;
    }
}
