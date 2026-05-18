package com.flower.repository;

import com.flower.entity.AbnormalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 异常订单数据访问层
 */
@Repository
public interface AbnormalOrderRepository extends JpaRepository<AbnormalOrder, Long>, JpaSpecificationExecutor<AbnormalOrder> {

    /**
     * 根据订单ID查询异常记录
     */
    List<AbnormalOrder> findByOrderId(Long orderId);

    /**
     * 根据状态查询异常订单
     */
    List<AbnormalOrder> findByStatus(Integer status);

    /**
     * 根据类型查询异常订单
     */
    List<AbnormalOrder> findByType(Integer type);
}
