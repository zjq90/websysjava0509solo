package com.photostudio.service;

import com.photostudio.entity.Employee;
import com.photostudio.entity.Schedule;
import com.photostudio.entity.TransferRequest;
import com.photostudio.entity.TransferRequest.RequestStatus;
import com.photostudio.repository.EmployeeRepository;
import com.photostudio.repository.ScheduleRepository;
import com.photostudio.repository.TransferRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 调班申请服务类
 * 提供调班申请管理相关的业务逻辑
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Service
public class TransferRequestService {

    private final TransferRequestRepository transferRequestRepository;
    private final ScheduleRepository scheduleRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public TransferRequestService(TransferRequestRepository transferRequestRepository,
                                  ScheduleRepository scheduleRepository,
                                  EmployeeRepository employeeRepository) {
        this.transferRequestRepository = transferRequestRepository;
        this.scheduleRepository = scheduleRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * 获取所有调班申请
     * @return 调班申请列表
     */
    public List<TransferRequest> getAllRequests() {
        return transferRequestRepository.findAll();
    }

    /**
     * 根据ID获取调班申请
     * @param id 申请ID
     * @return 调班申请信息
     */
    public Optional<TransferRequest> getRequestById(Long id) {
        return transferRequestRepository.findById(id);
    }

    /**
     * 根据申请人获取调班申请
     * @param requesterId 申请人ID
     * @return 调班申请列表
     */
    public List<TransferRequest> getRequestsByRequester(Long requesterId) {
        Employee requester = employeeRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("申请人不存在"));
        return transferRequestRepository.findByRequester(requester);
    }

    /**
     * 根据状态获取调班申请
     * @param status 状态
     * @return 调班申请列表
     */
    public List<TransferRequest> getRequestsByStatus(RequestStatus status) {
        return transferRequestRepository.findByStatus(status);
    }

    /**
     * 获取待审批的调班申请
     * @return 调班申请列表
     */
    public List<TransferRequest> getPendingRequests() {
        return transferRequestRepository.findByStatusOrderByCreateTimeDesc(RequestStatus.PENDING);
    }

    /**
     * 创建调班申请
     * @param request 调班申请信息
     * @return 创建的调班申请
     */
    @Transactional
    public TransferRequest createRequest(TransferRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getOriginalSchedule().getId())
                .orElseThrow(() -> new RuntimeException("排班不存在"));
        
        Employee requester = employeeRepository.findById(request.getRequester().getId())
                .orElseThrow(() -> new RuntimeException("申请人不存在"));
        
        request.setOriginalSchedule(schedule);
        request.setRequester(requester);
        
        if (request.getTargetEmployee() != null && request.getTargetEmployee().getId() != null) {
            Employee targetEmployee = employeeRepository.findById(request.getTargetEmployee().getId())
                    .orElseThrow(() -> new RuntimeException("目标员工不存在"));
            request.setTargetEmployee(targetEmployee);
        }
        
        schedule.setStatus(Schedule.ScheduleStatus.TRANSFER_REQUESTED);
        scheduleRepository.save(schedule);
        
        return transferRequestRepository.save(request);
    }

    /**
     * 审批调班申请
     * @param id 申请ID
     * @param approverId 审批人ID
     * @param approved 是否通过
     * @param remark 审批意见
     * @return 更新后的调班申请
     */
    @Transactional
    public TransferRequest approveRequest(Long id, Long approverId, boolean approved, String remark) {
        TransferRequest request = transferRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("调班申请不存在"));
        
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("申请状态不允许审批");
        }
        
        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("审批人不存在"));
        
        request.setApprover(approver);
        request.setApprovalRemark(remark);
        request.setApprovalTime(LocalDateTime.now());
        
        if (approved) {
            request.setStatus(RequestStatus.APPROVED);
            
            Schedule schedule = request.getOriginalSchedule();
            if (request.getTargetEmployee() != null) {
                schedule.setEmployee(request.getTargetEmployee());
            }
            schedule.setStatus(Schedule.ScheduleStatus.CONFIRMED);
            scheduleRepository.save(schedule);
        } else {
            request.setStatus(RequestStatus.REJECTED);
            
            Schedule schedule = request.getOriginalSchedule();
            schedule.setStatus(Schedule.ScheduleStatus.CONFIRMED);
            scheduleRepository.save(schedule);
        }
        
        return transferRequestRepository.save(request);
    }

    /**
     * 取消调班申请
     * @param id 申请ID
     * @return 更新后的调班申请
     */
    @Transactional
    public TransferRequest cancelRequest(Long id) {
        TransferRequest request = transferRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("调班申请不存在"));
        
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("申请状态不允许取消");
        }
        
        request.setStatus(RequestStatus.CANCELLED);
        
        Schedule schedule = request.getOriginalSchedule();
        schedule.setStatus(Schedule.ScheduleStatus.CONFIRMED);
        scheduleRepository.save(schedule);
        
        return transferRequestRepository.save(request);
    }

    /**
     * 删除调班申请
     * @param id 申请ID
     */
    @Transactional
    public void deleteRequest(Long id) {
        if (!transferRequestRepository.existsById(id)) {
            throw new RuntimeException("调班申请不存在");
        }
        transferRequestRepository.deleteById(id);
    }
}
