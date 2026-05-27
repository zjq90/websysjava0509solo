package com.club.repository;

import com.club.entity.DataBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据备份数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface DataBackupRepository extends JpaRepository<DataBackup, Long>, JpaSpecificationExecutor<DataBackup> {

    /**
     * 根据备份类型查询
     */
    List<DataBackup> findByType(String type);

    /**
     * 根据状态查询
     */
    List<DataBackup> findByStatus(Integer status);

    /**
     * 根据操作人ID查询
     */
    List<DataBackup> findByOperatorId(Long operatorId);

    /**
     * 查询最近成功的备份记录
     */
    List<DataBackup> findTop10ByStatusOrderByCreateTimeDesc(Integer status);
}
