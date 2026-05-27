package com.club.repository;

import com.club.entity.ViolationRecord;
import com.club.enums.ViolationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 违规记录数据访问接口
 *
 * @author Club Management System
 * @version 1.0.0
 */
@Repository
public interface ViolationRecordRepository extends JpaRepository<ViolationRecord, Long>, JpaSpecificationExecutor<ViolationRecord> {

    /**
     * 根据社团ID查询违规记录
     */
    List<ViolationRecord> findByClubId(Long clubId);

    /**
     * 根据违规类型查询记录
     */
    List<ViolationRecord> findByViolationType(ViolationType type);

    /**
     * 查询未整改的违规记录
     */
    List<ViolationRecord> findByRectifiedFalse();

    /**
     * 统计未整改违规记录数量
     */
    long countByRectifiedFalse();
}
