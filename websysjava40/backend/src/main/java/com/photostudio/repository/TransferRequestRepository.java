package com.photostudio.repository;

import com.photostudio.entity.Employee;
import com.photostudio.entity.TransferRequest;
import com.photostudio.entity.TransferRequest.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调班申请数据访问接口
 * 提供调班申请相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface TransferRequestRepository extends JpaRepository<TransferRequest, Long> {

    /**
     * 根据申请人查询调班申请
     * @param requester 申请人
     * @return 调班申请列表
     */
    List<TransferRequest> findByRequester(Employee requester);

    /**
     * 根据状态查询调班申请
     * @param status 状态
     * @return 调班申请列表
     */
    List<TransferRequest> findByStatus(RequestStatus status);

    /**
     * 查询待审批的调班申请
     * @param status 状态
     * @return 调班申请列表
     */
    List<TransferRequest> findByStatusOrderByCreateTimeDesc(RequestStatus status);
}
