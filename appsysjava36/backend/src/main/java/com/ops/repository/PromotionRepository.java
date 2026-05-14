package com.ops.repository;

import com.ops.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 营销活动数据访问接口
 * 
 * @author ops-admin
 */
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {

    /**
     * 查找进行中的活动
     */
    List<Promotion> findByStatusAndStartTimeBeforeAndEndTimeAfter(String status, LocalDateTime now1, LocalDateTime now2);

    /**
     * 根据目标用户类型查找活动
     */
    List<Promotion> findByTargetUserTypeAndStatus(String targetUserType, String status);
}
