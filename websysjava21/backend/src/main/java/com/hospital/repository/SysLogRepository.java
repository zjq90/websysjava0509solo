package com.hospital.repository;

import com.hospital.entity.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * 系统日志数据访问层
 * 
 * @author Hospital Management Team
 * @version 1.0.0
 */
@Repository
public interface SysLogRepository extends JpaRepository<SysLog, Long>, JpaSpecificationExecutor<SysLog> {
}
