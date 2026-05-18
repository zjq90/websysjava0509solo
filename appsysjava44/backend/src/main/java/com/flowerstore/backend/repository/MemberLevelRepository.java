package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.MemberLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会员等级数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface MemberLevelRepository extends JpaRepository<MemberLevel, Long> {

    /**
     * 根据状态查询会员等级列表（按升级金额降序）
     */
    List<MemberLevel> findByStatusOrderByUpgradeAmountDesc(Integer status);

    /**
     * 根据状态查询会员等级列表（按排序升序）
     */
    List<MemberLevel> findByStatusOrderBySortOrderAsc(Integer status);
}
