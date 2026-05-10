package com.websys.repository;

import com.websys.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志Repository接口
 * 
 * @author websys
 * @version 1.0.0
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {

    /**
     * 根据操作用户ID查询日志列表
     * 
     * @param userId 用户ID
     * @return 日志列表
     */
    List<OperationLog> findByUserIdOrderByCreateTimeDesc(Long userId);

    /**
     * 根据操作模块查询日志列表
     * 
     * @param module 操作模块
     * @return 日志列表
     */
    List<OperationLog> findByModuleOrderByCreateTimeDesc(String module);

    /**
     * 根据操作类型查询日志列表
     * 
     * @param operationType 操作类型
     * @return 日志列表
     */
    List<OperationLog> findByOperationTypeOrderByCreateTimeDesc(String operationType);

    /**
     * 根据时间范围查询日志列表
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 日志列表
     */
    List<OperationLog> findByCreateTimeBetweenOrderByCreateTimeDesc(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 删除指定时间之前的日志
     * 
     * @param time 时间阈值
     */
    void deleteByCreateTimeBefore(LocalDateTime time);
}
