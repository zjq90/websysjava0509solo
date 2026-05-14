package com.photostudio.repository;

import com.photostudio.entity.Express;
import com.photostudio.entity.Express.ExpressStatus;
import com.photostudio.entity.Express.LogisticsCompany;
import com.photostudio.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 快递数据访问接口
 * 提供快递相关的数据库操作方法
 * 
 * @author PhotoStudio Team
 * @version 1.0.0
 */
@Repository
public interface ExpressRepository extends JpaRepository<Express, Long> {

    /**
     * 根据订单查询快递信息
     * @param order 订单
     * @return 快递信息
     */
    Optional<Express> findByOrder(Order order);

    /**
     * 根据快递单号查询快递信息
     * @param trackingNo 快递单号
     * @return 快递信息
     */
    Optional<Express> findByTrackingNo(String trackingNo);

    /**
     * 根据物流公司查询快递列表
     * @param company 物流公司
     * @return 快递列表
     */
    List<Express> findByCompany(LogisticsCompany company);

    /**
     * 根据物流状态查询快递列表
     * @param status 物流状态
     * @return 快递列表
     */
    List<Express> findByStatus(ExpressStatus status);

    /**
     * 查询未签收的快递
     * @param status 状态
     * @return 快递列表
     */
    List<Express> findByStatusOrderByCreateTimeDesc(ExpressStatus status);
}
