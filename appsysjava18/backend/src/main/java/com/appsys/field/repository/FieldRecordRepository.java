package com.appsys.field.repository;

import com.appsys.field.entity.FieldRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 田间记录数据访问接口
 * 
 * @author 系统管理员
 * @version 1.0.0
 */
@Repository
public interface FieldRecordRepository extends JpaRepository<FieldRecord, Long>, JpaSpecificationExecutor<FieldRecord> {

    /**
     * 根据ID查询未删除的记录
     */
    Optional<FieldRecord> findByIdAndDeletedFalse(Long id);

    /**
     * 分页查询未删除的记录
     */
    Page<FieldRecord> findByDeletedFalseOrderByRecordDateDesc(Pageable pageable);

    /**
     * 根据地块名称模糊查询（分页）
     */
    @Query("SELECT f FROM FieldRecord f WHERE f.deleted = false AND " +
           "(f.fieldName LIKE %:keyword% OR f.cropName LIKE %:keyword%)")
    Page<FieldRecord> searchByKeyword(String keyword, Pageable pageable);

    /**
     * 根据日期范围查询
     */
    Page<FieldRecord> findByRecordDateBetweenAndDeletedFalseOrderByRecordDateDesc(
            LocalDate startDate, LocalDate endDate, Pageable pageable);
}
