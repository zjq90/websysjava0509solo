package com.hospital.repository;

import com.hospital.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志Repository接口
 * 提供操作日志数据访问层的基本操作
 * 
 * @author hospital
 * @version 1.0.0
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long>, JpaSpecificationExecutor<OperationLog> {

    /**
     * 查询用户的操作日志
     * 
     * @param userId 用户ID
     * @return 操作日志列表
     */
    List<OperationLog> findByUserIdOrderByOperationTimeDesc(Long userId);

    /**
     * 删除指定时间之前的日志（用于日志清理）
     * 
     * @param dateTime 截止时间
     */
    void deleteByOperationTimeBefore(LocalDateTime dateTime);

    /**
     * 按模块查询日志
     * 
     * @param module 模块
     * @return 操作日志列表
     */
    List<OperationLog> findByModuleOrderByOperationTimeDesc(String module);
}
