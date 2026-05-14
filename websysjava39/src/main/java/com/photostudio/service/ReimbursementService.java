package com.photostudio.service;

import com.photostudio.entity.Employee;
import com.photostudio.entity.Reimbursement;
import com.photostudio.entity.ReimbursementApprovalRecord;
import com.photostudio.exception.ResourceNotFoundException;
import com.photostudio.repository.EmployeeRepository;
import com.photostudio.repository.ReimbursementApprovalRecordRepository;
import com.photostudio.repository.ReimbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 报销服务类
 * 实现报销申请、审批流程和审批留痕功能
 * 
 * @author Photo Studio Team
 * @version 1.0.0
 */
@Service
public class ReimbursementService {

    @Autowired
    private ReimbursementRepository reimbursementRepository;

    @Autowired
    private ReimbursementApprovalRecordRepository approvalRecordRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 创建报销申请
     */
    @Transactional
    public Reimbursement createReimbursement(Reimbursement reimbursement) {
        // 生成报销编号
        String reimburseNo = generateReimburseNo();
        reimbursement.setReimburseNo(reimburseNo);
        reimbursement.setStatus("PENDING");
        
        Reimbursement saved = reimbursementRepository.save(reimbursement);
        
        // 记录审批日志（申请）
        ReimbursementApprovalRecord record = new ReimbursementApprovalRecord();
        record.setReimbursement(saved);
        record.setOperator(reimbursement.getEmployee());
        record.setOperationType("APPLY");
        record.setPreviousStatus(null);
        record.setCurrentStatus("PENDING");
        record.setRemark("提交报销申请");
        approvalRecordRepository.save(record);
        
        return saved;
    }

    /**
     * 审批报销申请
     */
    @Transactional
    public Reimbursement approveReimbursement(Long id, Long approverId, String status, String remark) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("报销记录不存在: " + id));
        
        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new ResourceNotFoundException("审批人不存在: " + approverId));
        
        // 记录审批前状态
        String previousStatus = reimbursement.getStatus();
        
        // 更新报销状态
        reimbursement.setStatus(status);
        reimbursement.setApprover(approver);
        reimbursement.setApproveRemark(remark);
        
        Reimbursement saved = reimbursementRepository.save(reimbursement);
        
        // 记录审批日志
        ReimbursementApprovalRecord record = new ReimbursementApprovalRecord();
        record.setReimbursement(saved);
        record.setOperator(approver);
        record.setOperationType("APPROVED".equals(status) ? "APPROVE" : "REJECT");
        record.setPreviousStatus(previousStatus);
        record.setCurrentStatus(status);
        record.setRemark(remark);
        approvalRecordRepository.save(record);
        
        return saved;
    }

    /**
     * 获取报销的审批记录
     */
    public List<ReimbursementApprovalRecord> getApprovalRecords(Long reimbursementId) {
        return approvalRecordRepository.findByReimbursementIdOrderByCreateTimeDesc(reimbursementId);
    }

    /**
     * 根据ID获取报销详情（包含审批记录）
     */
    public Reimbursement getReimbursementWithRecords(Long id) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("报销记录不存在: " + id));
        return reimbursement;
    }

    /**
     * 生成报销编号
     */
    private String generateReimburseNo() {
        String prefix = "BX";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return prefix + timestamp + random;
    }
}
