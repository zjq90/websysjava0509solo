package com.flowerstore.backend.repository;

import com.flowerstore.backend.entity.EnterpriseCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 企业定制数据访问接口
 * 
 * @author FlowerStore Team
 * @version 1.0.0
 */
@Repository
public interface EnterpriseCustomRepository extends JpaRepository<EnterpriseCustom, Long> {

    /**
     * 根据用户ID查询企业定制订单
     */
    List<EnterpriseCustom> findByUserIdOrderByCreateTimeDesc(Long userId);

    /**
     * 根据状态查询
     */
    List<EnterpriseCustom> findByStatusOrderByCreateTimeDesc(Integer status);
}
