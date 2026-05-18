package com.secondhand.repository;

import com.secondhand.entity.GroupBuyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 拼团记录Repository接口
 *
 * @author secondhand
 * @version 1.0.0
 */
@Repository
public interface GroupBuyRecordRepository extends JpaRepository<GroupBuyRecord, Long> {

    List<GroupBuyRecord> findByUserIdOrderByCreateTimeDesc(Long userId);

    List<GroupBuyRecord> findByGroupNoOrderByCreateTimeAsc(String groupNo);

    @Query("SELECT COUNT(r) FROM GroupBuyRecord r WHERE r.groupNo = ?1")
    int countByGroupNo(String groupNo);

    boolean existsByActivityIdAndUserId(Long activityId, Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE GroupBuyRecord r SET r.status = ?2 WHERE r.groupNo = ?1")
    void updateGroupStatus(String groupNo, String status);

}
